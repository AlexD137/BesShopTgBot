package ru.jmdevelop.besshoptgbot.domain.services.impl;

import ru.jmdevelop.besshoptgbot.domain.services.ProductMediaService;

@Service
@RequiredArgsConstructor
public class ProductMediaServiceImpl {
    private final ProductRepository productRepository;

    public void addPhotoToProduct(Long productId, List<PhotoSize> photoSizes) {
        Product product = productRepository.findById(productId);

        // Берем фото наибольшего размера (обычно последнее в списке)
        PhotoSize largestPhoto = photoSizes.get(photoSizes.size() - 1);
        product.getMedia().addPhoto(largestPhoto.getFileId());

        productRepository.save(product);
    }

    public void addVideoToProduct(Long productId, Video video) {
        Product product = productRepository.findById(productId);
        product.getMedia().addVideo(video.getFileId());
        productRepository.save(product);
    }

    public void setMediaCaption(Long productId, String caption) {
        Product product = productRepository.findById(productId);
        product.getMedia().setMediaCaption(caption);
        productRepository.save(product);
    }
}
