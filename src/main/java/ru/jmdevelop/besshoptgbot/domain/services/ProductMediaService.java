package ru.jmdevelop.besshoptgbot.domain.services;

public class ProductMediaService {

    public void addPhotoToProduct(Long productId, List<PhotoSize> photoSizes);
    public void addVideoToProduct(Long productId, Video video);
    public void setMediaCaption(Long productId, String caption);
}
