package kr.rootuser.tika.client.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 * 
 * @author Daniel
 *
 * References:
 * https://stackoverflow.com/questions/32354209/apache-tika-extract-scanned-pdf-files
 * https://www.woodmark.de/blog/parsing-text-within-image-files-or-embedded-images-pdfs-using-apache-tika-ocr/
 */

public class TikaClientTest {

	private static String filename = "ORG_english_contract.pdf";
//	private static String filename = "SCAN_english_contract.pdf";

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		FileInputStream stream = getFilePath();
		BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);

		Parser parser = new AutoDetectParser();
		TesseractOCRConfig config = new TesseractOCRConfig();

		PDFParserConfig pdfConfig = new PDFParserConfig();
		pdfConfig.setExtractInlineImages(true);

		ParseContext parseContext = new ParseContext();
		parseContext.set(TesseractOCRConfig.class, config);
		parseContext.set(PDFParserConfig.class, pdfConfig);
		// need to add this to make sure recursive parsing happens!
		parseContext.set(Parser.class, parser);

		Metadata metadata = new Metadata();
		parser.parse(stream, handler, metadata, parseContext);
		System.out.println(metadata);
		String content = handler.toString();
		System.out.println("===============");
		System.out.println(content);
		System.out.println("Done");
		
		/*ContentHandler contenthandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		PDFParser pdfparser = new PDFParser();
		pdfparser.parse(is, contenthandler, metadata, new ParseContext());*/
	}

	private static FileInputStream getFilePath() throws FileNotFoundException {
		ClassLoader classLoader = TikaClientTest.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		return new FileInputStream(file);
	}

}
