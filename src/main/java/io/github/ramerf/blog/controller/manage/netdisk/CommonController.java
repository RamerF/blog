package io.github.ramerf.blog.controller.manage.netdisk;

import io.github.ramerf.wind.core.entity.response.Rs;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import io.github.ramerf.blog.system.entity.Constant.AccessPath;
import io.github.ramerf.blog.system.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Ramer
 * @version 2019/5/31
 */
@Slf4j
@Controller
@RequestMapping(AccessPath.MANAGE + "/netdisk")
public class CommonController {
  @Value("${io.github.ramerf.blog.netdisk}")
  private String basePath;

  private static final String fileSeparator = File.separator;

  @GetMapping
  public String index() {
    return "manage/netdisk/index";
  }

  @GetMapping("/listFile")
  public ResponseEntity listFile(@RequestParam(value = "path", required = false) String path) {
    log.info(" CommonController.foo : [{}]", path);
    path = StringUtils.isEmpty(path) ? basePath : basePath.concat(fileSeparator).concat(path);
    File file = new File(path);
    if (!file.exists()) {
      return Rs.ok(new ArrayList<>());
    }
    basePath = new File(basePath).getAbsolutePath();
    return Rs.ok(
        Stream.of(Objects.requireNonNull(file.listFiles()))
            .map(
                f -> {
                  Folder folder = new Folder();
                  folder.setDir(f.isDirectory());
                  folder.setName(f.getName());
                  folder.setPath(
                      f.getAbsolutePath()
                          .substring(
                              f.getAbsolutePath().indexOf(basePath) + basePath.length() + 1));
                  folder.setFileType(getFileType(f.getName()));
                  return folder;
                })
            .collect(Collectors.toList()));
  }

  @GetMapping("/downloadFile")
  public ResponseEntity downloadFile(@RequestParam(value = "path", required = false) String path)
      throws Exception {
    if (StringUtils.isEmpty(path)) {
      return Rs.fail("文件不存在");
    }
    FileSystemResource file = new FileSystemResource(basePath.concat(fileSeparator).concat(path));
    if (!file.exists()) {
      return Rs.fail("文件不存在");
    }
    final String filename = file.getFilename();
    log.info(" CommonController.downloadFile : [{}]", filename);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add(
        "Content-Disposition",
        "attachment; "
            .concat("fileName=")
            .concat(
                new String(
                    Objects.requireNonNull(filename).getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1)));
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
        .headers(headers)
        .contentLength(file.contentLength())
        .contentType(MediaType.parseMediaType("application/octet-stream"))
        .body(new InputStreamResource(file.getInputStream()));
  }

  @PostMapping("/uploadFile")
  public ResponseEntity uploadFile(
      @RequestParam("file") MultipartFile multipartFile,
      @RequestParam(value = "path", required = false) String path) {
    log.info(" TestController.uploadFile : [{}]", multipartFile.getOriginalFilename());
    path = StringUtils.isEmpty(path) ? basePath : basePath.concat(fileSeparator).concat(path);
    File file;
    if ((file = new File(path)).exists() && file.isDirectory()) {
      try {
        FileCopyUtils.copy(
            multipartFile.getInputStream(),
            new FileOutputStream(
                new File(
                    file.getAbsolutePath()
                        .concat(fileSeparator)
                        .concat(
                            Objects.isNull(multipartFile.getOriginalFilename())
                                ? ImageUtil.getRandomFileName()
                                : multipartFile.getOriginalFilename()))));
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      return Rs.ok();
    }
    return Rs.fail("目录不存在");
  }

  @PostMapping("/createFolder")
  public ResponseEntity createFolder(
      @RequestParam(value = "path", required = false) String path,
      @RequestParam(value = "name", required = false) String name) {
    log.info(" TestController.createFolder : [{},{}]", path, name);
    if (StringUtils.isEmpty(name)) {
      return Rs.ok("请指定文件夹名称");
    }
    path = StringUtils.isEmpty(path) ? basePath : basePath.concat(fileSeparator).concat(path);
    File file;
    if ((file = new File(path)).exists()
        && file.isDirectory()
        && new File(path.concat(fileSeparator).concat(name)).mkdir()) {
      return Rs.ok("创建成功");
    }
    return Rs.fail("目录不存在");
  }

  @PostMapping("/deleteFile")
  public ResponseEntity deleteFile(@RequestParam(value = "path", required = false) String path) {
    if (StringUtils.isEmpty(path)) {
      return Rs.fail("文件不存在");
    }
    File file = new File(basePath.concat(fileSeparator).concat(path));
    if (!file.exists()) {
      return Rs.fail("文件不存在");
    }
    // 目录需要级联删除
    Stack<File> stack = new Stack<>();
    stack.push(file);
    while (stack.size() > 0) {
      final File toDel = stack.pop();
      final File[] children = toDel.listFiles();
      if (toDel.isDirectory() && Objects.requireNonNull(children).length > 0) {
        stack.push(toDel);
        Arrays.stream(Objects.requireNonNull(children)).forEach(stack::push);
      } else if (!toDel.delete()) {
        return Rs.fail("删除失败");
      }
    }
    return Rs.ok("删除成功");
  }

  @Data
  private static class Folder {
    private String name;
    private String path;
    private boolean isDir;
    private String fileType;
  }

  private String getFileType(String fileName) {
    final int index = fileName.lastIndexOf(".");
    return index == -1 ? "other" : fileName.substring(index);
  }
}
