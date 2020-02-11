package planet.info.skyline.model;

/**
 * Created by Abhishek on 16/02/17.
 */

public class ImageItem {
    public String  ImageName;
    public String  Description;
    public String  ImageURI;
    public String  UploadedImageId;

    public ImageItem(String imageName, String description, String imageURI, String UploadedImageId) {
        ImageName = imageName;
        Description = description;
        ImageURI = imageURI;
        UploadedImageId = UploadedImageId;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURI() {
        return ImageURI;
    }

    public void setImageURI(String imageURI) {
        ImageURI = imageURI;
    }

   /* public static ImageItem addItem() {
        return new ImageItem("", "", "");

    }*/

    public String getUploadedImageId() {
        return UploadedImageId;
    }

    public void setUploadedImageId(String uploadedImageId) {
        UploadedImageId = uploadedImageId;
    }
}