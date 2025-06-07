package com.pdf.printScreen;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@SpringBootApplication
@RestController
public class PrintScreenApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrintScreenApplication.class, args);
	}
	private static final String SAIDA_IMAGENS = "C:/saida-pdf-imagens";
	@PostMapping("/upload")
	public String uploadPdf(@RequestParam("file") MultipartFile file){
		try{
			File pasta = new File(SAIDA_IMAGENS);
			if(!pasta.exists()){
				pasta.mkdirs();
			}
			PDDocument documento = PDDocument.load(file.getInputStream());
			PDFRenderer renderer = new PDFRenderer(documento);
			System.out.println("Salvando imagens em: "+ pasta.getAbsolutePath());

			for (int i = 0; i < documento.getNumberOfPages(); i++) {
				BufferedImage imagem = renderer.renderImageWithDPI(i,200);
				String nomeImagem = String.format("pagina_%02d.png", i + 1);
				File arquivoImagem = new File(pasta,nomeImagem);
				ImageIO.write(imagem, "PNG", arquivoImagem);
				System.out.println("Salvou: " + nomeImagem);
			}
			documento.close();
			return "Imagens salvas em: " + pasta.getAbsolutePath();
		}catch (Exception exception){
			return "Erro ao processar PDF " + exception.getMessage();
		}
	}


}
