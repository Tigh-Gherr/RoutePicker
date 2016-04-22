package uni.tighearnan.routepicker.Ticket;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by tighearnan on 13/04/16.
 */
public class BarcodeGenerator {
    public static Bitmap generateBarcodeBitmap(String barcode, BarcodeFormat format, int w, int h) {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;

        w = w - ((w / 16) * 2);

        try {
            result = writer.encode(barcode, format, w, h);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for(int y = 0; y < height; y++) {
            int offset = y * width;
            for(int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }
}
