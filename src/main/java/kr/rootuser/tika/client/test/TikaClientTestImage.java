package kr.rootuser.tika.client.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.config.TikaConfig;
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
 *         Reference:
 *         https://stackoverflow.com/questions/41985163/unable-to-extract-content-directly-from-scanned-pdf-using-apache-tika-but-work
 *         https://digi.bib.uni-mannheim.de/tesseract/ - windows
 */
public class TikaClientTestImage {

	// private static String filename = "ORG_english_contract.pdf";
	private static String filename = "SCAN_english_contract.pdf";
	// private static String filename = "SCAN_english_contract.jpg";
	private static String tesseract = "C:\\Program Files (x86)\\Tesseract-OCR";
	// private static String tesseract = "D:\\Program\\tesseract-Win64";
	private static String tessdata = "C:\\Program Files (x86)\\Tesseract-OCR\\tessdata";

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		FileInputStream stream = getFilePath();
		BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);

		TikaConfig config = TikaConfig.getDefaultConfig();
		Parser parser = new AutoDetectParser(config);
		Metadata metadata = new Metadata();

		TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
		ocrConfig.setLanguage("eng");
		ocrConfig.setTesseractPath(tesseract);
		ocrConfig.setTessdataPath(tessdata);
		ocrConfig.setMaxFileSizeToOcr(Integer.MAX_VALUE);

		PDFParserConfig pdfConfig = new PDFParserConfig();
		pdfConfig.setExtractInlineImages(true);
		pdfConfig.setExtractUniqueInlineImagesOnly(false);

		ParseContext parseContext = new ParseContext();
		// need to add this to make sure recursive parsing happens!
		parseContext.set(Parser.class, parser);
		parseContext.set(TesseractOCRConfig.class, ocrConfig);
		parseContext.set(PDFParserConfig.class, pdfConfig);

		parser.parse(stream, handler, metadata, parseContext);
		// System.out.println(metadata);
		String content = handler.toString();
		System.out.println("===============");
		System.out.println(content);
		System.out.println("Done");

		/*
		 * ContentHandler contenthandler = new BodyContentHandler(); Metadata metadata =
		 * new Metadata(); PDFParser pdfparser = new PDFParser(); pdfparser.parse(is,
		 * contenthandler, metadata, new ParseContext());
		 */
	}

	private static FileInputStream getFilePath() throws FileNotFoundException {
		ClassLoader classLoader = TikaClientTestImage.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());
		return new FileInputStream(file);
	}

}
