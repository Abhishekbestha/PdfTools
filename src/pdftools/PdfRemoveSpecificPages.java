package pdftools;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.Arrays;
import org.apache.pdfbox.exceptions.COSVisitorException;


/**
 *
 * @author abhi
 */
public class PdfRemoveSpecificPages {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) throws COSVisitorException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input PDF file path: ");
        String inputPath = scanner.nextLine();

        System.out.print("Enter output PDF file path: ");
        String outputPath = scanner.nextLine();

        System.out.print("Enter comma-separated page numbers to remove (e.g., 1,2,3,4): ");
        String pagesToRemoveInput = scanner.nextLine();

        try {
            String[] pagesToRemoveArray = pagesToRemoveInput.split(",");
            int[] pagesToRemove = Arrays.stream(pagesToRemoveArray)
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            removePages(inputPath, outputPath, pagesToRemove);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void removePages(String inputPath, String outputPath, int[] pagesToRemove) throws IOException, COSVisitorException {
        PDDocument document = PDDocument.load(new File(inputPath));
        int numberOfPages = document.getNumberOfPages();

        boolean validPages = true;
        for (int page : pagesToRemove) {
            if (page < 1 || page > numberOfPages) {
                validPages = false;
                break;
            }
        }

        if (validPages) {
            Arrays.sort(pagesToRemove);
            for (int i = pagesToRemove.length - 1; i >= 0; i--) {
                document.removePage(pagesToRemove[i] - 1); // Index is 0-based
            }
            document.save(outputPath);
            document.close();
            System.out.println("Pages " + Arrays.toString(pagesToRemove) + " removed successfully.");
        } else {
            System.out.println("Invalid page numbers. Please choose valid page numbers to remove.");
        }
    }
    
}
