package com.example.demo.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//Controller内ではMaxUploadSizeExceededExceptionのメッセージが受け取れないため、
//ControllerAdviceを使用
@ControllerAdvice
public class ItemControllerAdvice {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleException(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
		System.out.println("ControllerAdviceが呼び出されました。");
        if (e instanceof MaxUploadSizeExceededException ||
            (e.getCause() != null && e.getCause() instanceof MaxUploadSizeExceededException)) {
        	
            redirectAttributes.addFlashAttribute(
                    "MaxUploadSizeExceededMessage",
                    "ファイルサイズが大きすぎます");
            return "redirect:/admin/application";
        }

        return "redirect:/admin/application";
    }
}