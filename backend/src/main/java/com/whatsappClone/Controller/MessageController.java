package com.whatsappClone.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.whatsappClone.Exception.ChatException;
import com.whatsappClone.Exception.MessageException;
import com.whatsappClone.Exception.UserException;
import com.whatsappClone.Model.Message;
import com.whatsappClone.Model.User;
import com.whatsappClone.Payload.ApiResponse;
import com.whatsappClone.Payload.SendMessageRequest;
import com.whatsappClone.ServiceImpl.MessageServiceImpl;
import com.whatsappClone.ServiceImpl.UserServiceImpl;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest sendMessageRequest,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = this.userService.findUserProfile(jwt);

        sendMessageRequest.setUserId(user.getId());
        System.out.println(sendMessageRequest);
        Message message = this.messageService.sendMessage(sendMessageRequest);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<List<Message>> getChatMessageHandler(@PathVariable Integer chatId,
                                                               @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = this.userService.findUserProfile(jwt);

        List<Message> messages = this.messageService.getChatsMessages(chatId, user);

        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId,
                                                            @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException {

        User user = this.userService.findUserProfile(jwt);

        this.messageService.deleteMessage(messageId, user);

        ApiResponse res = new ApiResponse("Deleted successfully......", false);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PostMapping("/upload")
    public ResponseEntity<Message> uploadFileHandler(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chatId") Integer chatId,
            @RequestParam(value = "content", required = false) String content,
            @RequestHeader("Authorization") String jwt) throws IOException, UserException, ChatException {

        User user = this.userService.findUserProfile(jwt);

        Message message = new Message();
        if (content != null && !content.isEmpty()) {
            message.setContent(content);
        }

        message.setFileData(file.getBytes());
        message.setFileName(file.getOriginalFilename());
        message.setFileType(file.getContentType());
        message.setTimestamp(java.time.LocalDateTime.now());
        message.setUser(user);
        message.setChat(this.messageService.findChatById(chatId));

        Message savedMessage = this.messageService.saveMessage(message);
        String generatedContent = "localhost:8080/api/messages/download/" + savedMessage.getId();
        savedMessage.setContent(generatedContent);

        // Save the updated message
        savedMessage = this.messageService.saveMessage(savedMessage);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    // Download a file from a message
    @GetMapping("/download/{messageId}")
    public ResponseEntity<byte[]> downloadFileHandler(
            @PathVariable Integer messageId,
            @RequestHeader("Authorization") String jwt) throws UserException, MessageException {

        User user = this.userService.findUserProfile(jwt);
        Message message = this.messageService.findMessageById(messageId, user);

        if (message.getFileData() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(message.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + message.getFileName() + "\"")
                .body(message.getFileData());
    }
}
