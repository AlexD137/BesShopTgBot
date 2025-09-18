package ru.jmdevelop.besshoptgbot.domain.models.product;

import lombok.Data;

import java.util.ArrayList;


public class ProductMedia {
    @Data
    public class ProductMedia {
        private Long productId;
        private List<Photo> photos = new ArrayList<>();
        private List<Video> videos = new ArrayList<>();

        public void addPhoto(Photo photo) {
            photos.add(photo);
        }

        public Optional<Photo> getMainPhoto() {
            return photos.stream()
                    .filter(Photo::isMain)
                    .findFirst();
        }

        public boolean hasMedia() {
            return !photos.isEmpty() || !videos.isEmpty();
        }
    }
}
