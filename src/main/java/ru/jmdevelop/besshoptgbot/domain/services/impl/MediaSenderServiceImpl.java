package ru.jmdevelop.besshoptgbot.domain.services.impl;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MediaSenderServiceImpl implements MediaSenderService {
    private final TelegramBot telegramBot;
    private final ProductRepository productRepository;

    public void sendProductMedia(Long productId, Long chatId) {
        Product product = productRepository.findById(productId);
        ProductMedia media = product.getMedia();

        // Отправляем фото
        for (ProductMedia.TelegramMedia photo : media.getPhotos()) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId.toString())
                    .photo(new InputFile(photo.getFileId()))
                    .caption(media.getMediaCaption())
                    .build();

            telegramBot.execute(sendPhoto);
        }

        // Отправляем видео
        for (ProductMedia.TelegramMedia video : media.getVideos()) {
            SendVideo sendVideo = SendVideo.builder()
                    .chatId(chatId.toString())
                    .video(new InputFile(video.getFileId()))
                    .caption(media.getMediaCaption())
                    .build();

            telegramBot.execute(sendVideo);
        }
    }
}