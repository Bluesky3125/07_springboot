package com.ohgiraffers.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FileUploadController {

    @Value("${filepath}")
    private String filePath;

    /* 설명. multipart/form-data로 넘어오는 것은 MultipartFile로 받아내야 한다. */
    @PostMapping("single-file")
    public String singleFile(@RequestParam MultipartFile singleFile,
                             @RequestParam String singleFileDescription,
                             RedirectAttributes rttr) {
//        System.out.println("singleFile = " + singleFile);
//        System.out.println("singleFileDescription = " + singleFileDescription);

        /* 목차. 1. 저장될 파일의 경로 설정 */
//        String filePath = "C:/uploadFiles";

        /* 목차. 2. 파일의 이름 리네임 */
        /* 설명. 사용자가 넘긴 파일의 원본 이름을 확인하고 rename해보자.
         *   (자바의 UUID 클래스를 이용한 무작위 문자열 형태로 생성
         * */
        String originalFileName = singleFile.getOriginalFilename();
        System.out.println("originalFileName = " + originalFileName);
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        System.out.println("ext = " + ext);

//        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
        java.util.Date date = new java.util.Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String savedName = format.format(date) + ext;
        System.out.println("savedName = " + savedName);

        /* 목차. 3. 지정한 경로에 파일 저장 */
        try {
            singleFile.transferTo(new File(filePath + "/img/single/" + savedName));

            /* 목차. 4. DB로 보낼 데이터 만들기(Map<String, String>, List<Map<String, String>>) */
            /* 설명. DB로 보낼 데이터 Map으로 가공 처리 */
            Map<String, String> file = new HashMap<>();
            file.put("originalFileName", originalFileName);
            file.put("savedName", savedName);
            file.put("filePath", "/img/single");
            file.put("singleFileDescription", singleFileDescription);

            /* 설명. 이후 service 계층을 통해 DB에 사용자가 업로드한 파일의 내용을 저장하고 옴 */
//            fileUploadService.registFile(file); 같은 식으로 할 예정

            rttr.addFlashAttribute("message", originalFileName + " 파일 업로드 성공");
            rttr.addFlashAttribute("img", "/img/single/" + savedName);
            rttr.addFlashAttribute("singleFileDescription", singleFileDescription);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/result";
    }

    @PostMapping("multi-file")
    public String multifileUpload(@RequestParam List<MultipartFile> multiFiles,
                                  @RequestParam String multiFileDescription,
                                  RedirectAttributes rttr) {

//        String filePath = "C:/uploadFiles";

        /* 설명. DB에 보낼 값을 담기 위한 컬렉션 */
        List<Map<String, String>> files = new ArrayList<>();

        /* 설명. 화면에서 각 파일마다 img 태그의 src 속성으로 적용하기 위한 문자열을 담은 컬렉션 */
        List<String> imgSrcs = new ArrayList<>();

        try {
            for (int i = 0; i < multiFiles.size(); i++) {

                /* 설명. 각 파일마다 리네임 */
                String originalFileName = multiFiles.get(i).getOriginalFilename();
                String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
                java.util.Date date = new java.util.Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String savedName = format.format(date) + "-" + (i + 1) + ext;

                /* 설명. 파일 저장 경로에 저장 */
                multiFiles.get(i).transferTo(new File(filePath + "/img/multi/" + savedName));

                /* 설명. DB에 보낼 값 설정(Map<String, String>) */
                Map<String, String> file = new HashMap<>();
                file.put("originalFileName", originalFileName);
                file.put("savedName", savedName);
                file.put("filePath", "/img/multi");
                file.put("multiFileDescription", multiFileDescription);

                files.add(file);
                imgSrcs.add("/img/multi/" + savedName);
            }   // for end

            /* 설명. singleFile 업로드 때와 마찬가지로 DB를 다녀옴 */
//            fileUploadService.registFile(files);

            /* 설명. 여기까지 성공했다면 파일 저장 및 DB insert까지 모두 완성되었으니 화면의 재료 작성 */
            rttr.addFlashAttribute("message", "다중 파일 업로드 성공");
            rttr.addFlashAttribute("imgs", imgSrcs);
            rttr.addFlashAttribute("multiFileDescription", multiFileDescription);
        } catch (IOException e) {
            for (int i = 0; i < files.size(); i++) {
                Map<String, String> file = files.get(i);
                new File(filePath + "/img/multi/" + file.get("savedName")).delete();
            }

            rttr.addFlashAttribute("message", "파일 업로드 실패");
        }

        return "redirect:/result";
    }

    @GetMapping("result")
    public void result() {
    }
}
