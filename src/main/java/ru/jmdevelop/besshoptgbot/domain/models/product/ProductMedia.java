package ru.jmdevelop.besshoptgbot.domain.models.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
public class ProductMedia {
    private Long productId;
    private List<TelegramMedia> photos = new ArrayList<>();
    private List<TelegramMedia> videos = new ArrayList<>();
    private String mediaCaption;

    @Data
    public static class TelegramMedia {
        private String fileId;
        private Integer fileSize;
        private Integer width;
        private Integer height;
        private Integer displayOrder;
        private boolean isMain;

        public TelegramMedia(String fileId) {
            this.fileId = fileId;
            this.displayOrder = 0;
            this.isMain = false;
        }
    }

    public void addPhoto(String fileId) {
        TelegramMedia media = new TelegramMedia(fileId);
        // Первое фото - главное
        if (photos.isEmpty()) {
            media.setMain(true);
        }
        photos.add(media);
    }

    public void addVideo(String fileId) {
        videos.add(new TelegramMedia(fileId));
    }

    public Optional<String> getMainPhotoFileId() {
        return photos.stream()
                .filter(TelegramMedia::isMain)
                .findFirst()
                .map(TelegramMedia::getFileId);
    }

    public List<String> getAllPhotoFileIds() {
        return photos.stream()
                .map(TelegramMedia::getFileId)
                .collect(Collectors.toList());
    }

    public boolean hasMedia() {
        return !photos.isEmpty() || !videos.isEmpty();
    }
}