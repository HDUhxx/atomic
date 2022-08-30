package co.uk.rushorm.ohos;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import co.uk.rushorm.core.RushFile;
import ohos.media.image.ImagePacker;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;

/**
 * Created by Stuart on 19/01/15.
 */
public class RushBitmapFile extends RushFile {

    public RushBitmapFile(String directory) {
        super(directory);
    }

    public RushBitmapFile() {
    }

    @Override
    public String fileExtension() {
        return "png";
    }

    public void setImage(PixelMap bitmap) throws IOException {
        ImagePacker imagePacker=ImagePacker.create();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        ImagePacker.PackingOptions packingOptions=new ImagePacker.PackingOptions();
        packingOptions.format = "image/png";
        packingOptions.quality = 100;
        imagePacker.initializePacking(stream.toByteArray(),packingOptions);
        writeToFile(stream.toByteArray());
    }

    public PixelMap getImage() throws IOException {
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        srcOpts.formatHint = "image/png";
        ImageSource imageSource=ImageSource.create(readFormFile(),srcOpts);
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(100, 100);
        decodingOpts.desiredRegion = new Rect(0, 0, 100, 100);
        decodingOpts.rotateDegrees = 90;
        return imageSource.createPixelmap(decodingOpts);
//        return BitmapFactory.decodeStream(readFormFile());
    }
}
