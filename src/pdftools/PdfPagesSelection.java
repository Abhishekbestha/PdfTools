package pdftools;

/**
 *
 * @author abhi
 */

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDPage;

public class PdfPagesSelection {

    public static void retainOnlySelectedPages(String inputPath, String outputPath, int[] pagesToRetain) throws IOException, COSVisitorException {
        PDDocument originalDocument = PDDocument.load(new File(inputPath));
        PDDocument newDocument = new PDDocument();

        @SuppressWarnings("unchecked")
        List<PDPage> allPages = originalDocument.getDocumentCatalog().getAllPages();
        int totalPages = allPages.size();

        boolean validPages = true;
        for (int page : pagesToRetain) {
            if (page < 1 || page > totalPages) {
                validPages = false;
                break;
            }
        }

        if (validPages) {
            for (int page : pagesToRetain) {
                newDocument.addPage(allPages.get(page - 1)); // 1-based to 0-based
            }

            newDocument.save(outputPath);
            System.out.println("Only pages " + Arrays.toString(pagesToRetain) + " retained successfully.");
        } else {
            System.out.println("Invalid page numbers. Please choose valid page numbers to retain.");
        }

        originalDocument.close();
        newDocument.close();
    }
    
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

            retainOnlySelectedPages(inputPath, outputPath, pagesToRemove);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
