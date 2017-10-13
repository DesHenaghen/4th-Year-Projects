import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Stego {

    public static void main(String args[]) {
        Stego s = new Stego();
        try {
            String decision = "";
            while (!decision.equals("q")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Please Select a Mode:\n1) Hide String\n2) Extract String\n3) Hide File\n4) Extract File\nq) Quit");
                decision = reader.readLine();
                if (decision.equals("1")) {
                    System.out.print("Cover Image: ");
                    String coverImage = reader.readLine();
                    System.out.print("Payload String: ");
                    String payloadString = reader.readLine();
                    System.out.println(s.hideString(payloadString, coverImage));
                } else if (decision.equals("2")) {
                    System.out.print("Image: ");
                    String image = reader.readLine();
                    System.out.println(s.extractString(image));
                } else if (decision.equals("3")) {
                    System.out.print("Cover Image: ");
                    String coverImage = reader.readLine();
                    System.out.print("Payload File: ");
                    String payloadFile = reader.readLine();
                    System.out.println(s.hideFile(payloadFile, coverImage));
                } else if (decision.equals("4")) {
                    System.out.print("Image: ");
                    String image = reader.readLine();
                    System.out.println(s.extractFile(image));
                } else {
                    System.out.println("I'm sorry Dave, I'm afraid I can't do that");
                }
            }
            //String coverImage = "Sun Painted Mountains over Medicine Lake 1680x1050.bmp";
            //System.out.println(s.extractString(s.hideString("what do you do with a drunken sailor?", coverImage)));
            //System.out.println(s.extractFile(s.hideFile("Hey_there_sexy.mp3", coverImage)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * A constant to hold the number of bits per byte
     */
    private final int byteLength = 8;

    /**
     * A constant to hold the number of bits used to store the size of the file extracted
     */
    protected final int sizeBitsLength = 32;
    /**
     * A constant to hold the number of bits used to store the extension of the file extracted
     */
    protected final int extBitsLength = 64;

    /**
     * Length of file info at beginning of file in bytes
     */
    protected final int fileInfoLength = 54;

    /**
     * Default constructor to create a stego object, doesn't do anything - so we actually don't need to declare it explicitly. Oh well.
     */
    public Stego() {
    }

    /**
     * A method for hiding a string in an uncompressed image file such as a .bmp or .png
     * You can assume a .bmp will be used
     *
     * @param cover_filename - the filename of the cover image as a string including the extension
     * @param payload        - the string which should be hidden in the cover image.
     * @return a string which either contains 'Fail' or the name of the stego image (including the extension) which has been
     * written out as a result of the successful hiding operation.
     * You can assume that the images are all in the same directory as the java files
     */
    public String hideString(String payload, String cover_filename) {
        String modifiedFilename = "mod" + cover_filename;
        try {
            // Append NULL character to mark end of payload
            payload += '\0';

            // Create byte[] from payload string
            byte[] payloadBytes = payload.getBytes();

            // Read bmp file from file
            BufferedImage origImage = ImageIO.read(new File(cover_filename));
            // Get image width in bytes (3 bytes per pixel)
            int imageWidth = origImage.getWidth() * 3;
            // Get the expected image width (an image with 0 padding bytes)
            int expectedWidth = (imageWidth + 3) /4 * 4;

            // System.out.println(imageWidth + " " + expectedWidth);

            // Convert BufferedImage to byte[] for manipulation
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(origImage, "bmp", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // System.out.println(imageBytes.length);
            // Insert payload into image byte array
            imageBytes = insertPayloadIntoImageBytes(payloadBytes, imageBytes, fileInfoLength, expectedWidth, imageWidth);

            // Convert image byte[] back into a BufferedImage
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage modImage = ImageIO.read(bais);

            // Finally write modified image to file
            ImageIO.write(modImage, "bmp", new File(modifiedFilename));

            return modifiedFilename;
        } catch (Exception e) {
            System.out.println(e.toString());
            return "FAIL";
        }
    }

    /**
     * The extractString method should extract a string which has been hidden in the stegoimage
     *
     * @param stego_image - the name of the stego image including the extension
     * @return a string which contains either the message which has been extracted or 'Fail' which indicates the extraction
     * was unsuccessful
     */
    public String extractString(String stego_image) {
        try {
            // Read bmp file from file
            BufferedImage origImage = ImageIO.read(new File(stego_image));

            // Convert BufferedImage to byte[] for manipulation
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(origImage, "bmp", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Get image width in bytes (3 bytes per pixel)
            int imageWidth = origImage.getWidth() * 3;
            // Get the expected image width (an image with 0 padding bytes)
            int expectedWidth = (imageWidth + 3) /4 * 4;

            StringBuilder payload = new StringBuilder();

            // Loop through bytes in image, extracting LSB from each
            boolean foundNull = false;
            int p = fileInfoLength;
            while (p < imageBytes.length) {
                int currentChar = 0;
                // Calculate int value of each byte hidden in image
                for (int j = byteLength - 1; j >= 0; j--) {
                    if (p >= imageBytes.length) break;
                    currentChar += ((imageBytes[p] & 0x1) << j);
                    //System.out.println(String.format(p+": %8s", Integer.toBinaryString(imageBytes[p] & 0xFF)).replace(' ', '0'));
                    p++;

                    // Skip every 4th byte and every byte that might fall into the buffer
                    while (((p - 1 % fileInfoLength) % 4 == 0) || ((p - fileInfoLength) % expectedWidth >= imageWidth)) {
                        //System.out.println(p + " "+ ((p - 1 % fileInfoLength) % 4 == 0) + " " + ((p - fileInfoLength) % expectedWidth >= imageWidth));
                        p++;
                    }
                }

                // If NULL character is detected, break loop
                if (currentChar == 0) {
                    foundNull = true;
                    break;
                } else {
                    payload.append((char) currentChar);
                }
            }

            if (!foundNull) throw new Exception("No null character found in payload. Either no message is in this file or the message was too long.");

            return payload.toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return "FAIL";
        }
    }

    //TODO you must write this method

    /**
     * The hideFile method hides any file (so long as there's enough capacity in the image file) in a cover image
     *
     * @param file_payload - the name of the file to be hidden including the extension, you can assume it is in the same directory as the program
     * @param cover_image  - the name of the cover image file including the extension, you can assume it is in the same directory as the program
     * @return String - either 'Fail' to indicate an error in the hiding process, or the name of the stego image (including the extension) written out as a result of the successful hiding process
     */
    public String hideFile(String file_payload, String cover_image) {
        String modifiedFilename = "mod" + cover_image;
        try {
            // Create byte[] from payload file
            Path fileLocation = Paths.get(file_payload);
            byte[] fileBytes = Files.readAllBytes(fileLocation);
            String fileExtension = getFileExtension(file_payload);
            byte[] fileExtensionBytes = fileExtension.getBytes();
            int fileSize = fileBytes.length;

            //System.out.println("file size in bytes "+fileSize);

            // Read bmp file from file
            BufferedImage origImage = ImageIO.read(new File(cover_image));
            // Get image width in bytes (3 bytes per pixel)
            int imageWidth = origImage.getWidth() * 3;
            // Get the expected image width (an image with 0 padding bytes)
            int expectedWidth = (imageWidth + 3) /4 * 4;

            // Convert BufferedImage to byte[] for manipulation
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(origImage, "bmp", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            int imageIterator = fileInfoLength;
            String fileSizeString = Integer.toBinaryString(fileSize);
            fileSizeString = String.format("%0"+(32-fileSizeString.length())+"d%s", 0, fileSizeString);
            //System.out.println("FILESIZESTRING: "+fileSizeString);
            for (int i = 0; i < sizeBitsLength;) {
                // Extract bits from payloadBytes using a mask and loop
                for (int j = byteLength - 1; j >= 0; j--, i++) {
                    if (imageIterator == imageBytes.length) throw new Exception("Cover image is not large enough to hide the payload");
                    // Extract next bit from payload
                    int bit = ((int)fileSizeString.charAt(i)) - 48;
                    //System.out.print(bit);
                    // Insert bit into next byte in image
                    //System.out.println(imageIterator);
                    imageBytes[imageIterator] = (byte) swapLSB(bit, imageBytes[imageIterator]);
                    //System.out.println(String.format(imageIterator+": %8s", Integer.toBinaryString(imageBytes[imageIterator] & 0xFF)).replace(' ', '0'));

                    // Increment imageIterator to select next image byte
                    imageIterator++;

                    // Skip every 4th byte and every byte that might fall into the buffer
                    while (((imageIterator - 1 % fileInfoLength) % 4 == 0) || ((imageIterator - fileInfoLength) % expectedWidth >= imageWidth)) {
                        //System.out.println(imageIterator);
                        imageIterator++;
                    }
                }
            }

            for (int i = 0; i < extBitsLength/8; i++) {
                // Get next byte of fileExtensionBytes, or 0x0 if fileExtensionBytes has ran out
                byte b;
                if (i < fileExtensionBytes.length) {
                    b = fileExtensionBytes[i];
                } else {
                    b = 0x0;
                }

                // Extract bits from fileExtensionBytes using a mask and loop
                for (int j = byteLength - 1; j >= 0; j--) {
                    if (imageIterator == imageBytes.length) throw new Exception("Cover image is not large enough to hide the payload");
                    // Extract next bit from payload
                    int bit = (b >> j) & 1;
                    // Insert bit into next byte in image
                    //System.out.println(imageIterator);
                    imageBytes[imageIterator] = (byte) swapLSB(bit, imageBytes[imageIterator]);
                    //System.out.println(String.format(imageIterator+": %8s", Integer.toBinaryString(imageBytes[imageIterator] & 0xFF)).replace(' ', '0'));

                    // Increment imageIterator to select next image byte
                    imageIterator++;

                    // Skip every 4th byte and every byte that might fall into the buffer
                    while (((imageIterator - 1 % fileInfoLength) % 4 == 0) || ((imageIterator - fileInfoLength) % expectedWidth >= imageWidth)) {
                        //System.out.println(imageIterator);
                        imageIterator++;
                    }
                }
            }

            // Insert payload into image byte array
            imageBytes = insertPayloadIntoImageBytes(fileBytes, imageBytes, imageIterator, expectedWidth, imageWidth);

/*            String mm = "";
            for (int i = fileInfoLength; i < imageBytes.length; i++) {
                if ((i - 1 % fileInfoLength) % 4 == 0) i++;
                if (i >= imageBytes.length) break;
                if (i > 136) break;
                mm += (imageBytes[i] & 0x1);
            }
            System.out.println("Payload: " + mm);*/

            // Convert image byte[] back into a BufferedImage
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage modImage = ImageIO.read(bais);

            // Finally write modified image to file
            ImageIO.write(modImage, "bmp", new File(modifiedFilename));

            return modifiedFilename;
        } catch (Exception e) {
            System.out.println(e.toString());
            return "FAIL";
        }
    }

    //TODO you must write this method

    /**
     * The extractFile method extracts a file from a stego image
     *
     * @param stego_image - name of the cover image (including the extension) from which to extract, you can assume it is in the same directory as the program
     * @return String - either 'Fail' to indicate an error in the extraction process, or the name of the file (including the extension) written out as a
     * result of the successful extraction process
     */
    public String extractFile(String stego_image) {
        try {
            // Read bmp file from file
            BufferedImage origImage = ImageIO.read(new File(stego_image));

            // Convert BufferedImage to byte[] for manipulation
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(origImage, "bmp", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Get image width in bytes (3 bytes per pixel)
            int imageWidth = origImage.getWidth() * 3;
            // Get the expected image width (an image with 0 padding bytes)
            int expectedWidth = (imageWidth + 3) /4 * 4;

            StringBuilder extension = new StringBuilder();
            int size = 0;

            // Loop through bytes in image, extracting LSB from each
            boolean foundNull = false;
            int p = fileInfoLength;
            int bitPointer = 0, bytePointer = 0;
            byte[] fileBytes = new byte[0];
            boolean metaExtracted = false, noFileBytes = true;
            while (p < imageBytes.length) {
                int currentChar = 0;
                if (metaExtracted && noFileBytes) {
                    fileBytes = new byte[size];
                    noFileBytes = false;
                }
                //if (metaExtracted) System.out.println(bytePointer-((extBitsLength+sizeBitsLength)/8)+ " size: "+size);
                if (metaExtracted && bytePointer > size) {
                    //System.out.println("WUT");
                    break;
                }
                // Calculate int value of each byte hidden in image
                for (int j = byteLength - 1; j >= 0; j--) {
                    if (p >= imageBytes.length) break;
                    currentChar += ((imageBytes[p] & 0x1) << j);
                    //System.out.println(String.format(p+": %8s", Integer.toBinaryString(imageBytes[p] & 0xFF)).replace(' ', '0'));
                    p++;

                    // Skip every 4th byte and every byte that might fall into the buffer
                    while (((p - 1 % fileInfoLength) % 4 == 0) || ((p - fileInfoLength) % expectedWidth >= imageWidth)) {
                        //System.out.println(p + " "+ ((p - 1 % fileInfoLength) % 4 == 0) + " " + ((p - fileInfoLength) % expectedWidth >= imageWidth));
                        p++;
                    }
                    bitPointer++;
                }
                //System.out.println(bitPointer);

                if (bitPointer <= sizeBitsLength) {
                    size += (currentChar << (32-bitPointer));
                    //System.out.println(size);
                } else if (bitPointer <= sizeBitsLength+extBitsLength) {
                    //payload.append((char) currentChar);
                    extension.append((char) currentChar);
                    metaExtracted = true;
                    bytePointer = 0;
                } else {
                    //System.out.println(bitPointer + " " + bytePointer+ " "+size);
                    fileBytes[bytePointer-1] = (byte) currentChar;
                    //System.out.println((char) currentChar);
                }

                bytePointer++;
            }

            //System.out.println("file size in bits is " + size);
            //System.out.println("file extension is file."+extension);

            String filename = "hiddenFile."+extension.toString().trim();
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(fileBytes);
            fos.close();

            return filename;
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "FAIL";
        }
    }

    /**
     * This method swaps the least significant bit of a byte to match the bit passed in
     *
     * @param bitToHide - the bit which is to replace the lsb of the byte
     * @param byt       - the current byte
     * @return the altered byte
     */
    private int swapLSB(int bitToHide, int byt) {
        int result;
        if (bitToHide == 0) {
            result = byt & ~0b1;
        } else {
            result = byt | 0b1;
        }
        return result;
    }

    private static String getFileExtension(String path) {
        String fileName = new File(path).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * Given payload & image byte arrays, insert the payload into the image
     *
     * @param payloadBytes - byte array of the payload to be hidden
     * @param imageBytes - byte array of the image to hide the payload in
     * @param expectedWidth - the width the image would need to be to avoid padding
     * @param imageWidth - the actual width of the image
     * @return The edited image byte array
     */
    private byte[] insertPayloadIntoImageBytes(byte[] payloadBytes, byte[] imageBytes, int imageIterator, int expectedWidth, int imageWidth) throws Exception{
        int i = 0;
        for (byte b : payloadBytes) {
            i++;
            // Extract bits from payloadBytes using a mask and loop
            for (int j = byteLength - 1; j >= 0; j--) {
                if (imageIterator == imageBytes.length) throw new Exception("Cover image is not large enough to hide the payload");
                // Extract next bit from payload
                int bit = (b >> j) & 1;
                // Insert bit into next byte in image
                //System.out.println(imageIterator);
                imageBytes[imageIterator] = (byte) swapLSB(bit, imageBytes[imageIterator]);
                //System.out.println(String.format(imageIterator+": %8s", Integer.toBinaryString(imageBytes[imageIterator] & 0xFF)).replace(' ', '0'));

                // Increment imageIterator to select next image byte
                imageIterator++;

                // Skip every 4th byte and every byte that might fall into the buffer
                while (((imageIterator - 1 % fileInfoLength) % 4 == 0) || ((imageIterator - fileInfoLength) % expectedWidth >= imageWidth)) {
                    //System.out.println(imageIterator);
                    imageIterator++;
                }
            }
        }

        //System.out.println("GESHSEHSEHES "+i);

        return imageBytes;
    }

}