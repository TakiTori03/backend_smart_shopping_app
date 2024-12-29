package com.hust.smart_Shopping.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hust.smart_Shopping.models.User;
import com.hust.smart_Shopping.repositories.UserRepository;
import com.hust.smart_Shopping.utils.RedirectUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MvcAdminController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Authentication authentication) {
        // if (!RedirectUtil.isAuthenticatedByAdmin(authentication)) {
        // return "redirect:/login";
        // }

        log.debug("Accessing home page");
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        if (RedirectUtil.isAuthenticatedByAdmin(authentication)) {
            return "redirect:/";
        }

        log.debug("Accessing login page");
        return "login";
    }

    @GetMapping("/user")
    public String getAllUsers(Model model, @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            List<User> users = new ArrayList<User>();
            Pageable paging = PageRequest.of(page - 1, size);

            Page<User> pageUsers;

            if (keyword == null) {
                pageUsers = userRepository.findAll(paging);
            } else {
                pageUsers = userRepository.findByNameContainingIgnoreCase(keyword, paging);
                model.addAttribute("keyword", keyword);
            }

            users = pageUsers.getContent();

            model.addAttribute("users", users);
            model.addAttribute("currentPage", pageUsers.getNumber() + 1);
            model.addAttribute("totalItems", pageUsers.getTotalElements());
            model.addAttribute("totalPages", pageUsers.getTotalPages());
            model.addAttribute("pageSize", size);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "user-all";
    }
}
