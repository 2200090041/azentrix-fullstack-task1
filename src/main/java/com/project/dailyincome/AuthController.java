package com.project.dailyincome;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @PostMapping("/register")
    public String register(User user, Model model) {

        User existingUser =
                userService.findByUsername(user.getUsername());

        if (existingUser != null) {

            model.addAttribute(
                    "error",
                    "Username already exists");

            return "register";
        }

        // PASSWORD VALIDATION STARTS HERE

        String password = user.getPassword();

        if(password.length() < 8 ||
           !password.matches(".*[A-Z].*") ||
           !password.matches(".*[a-z].*") ||
           !password.matches(".*\\d.*") ||
           !password.matches(".*[@#$%^&+=!].*")) {

            model.addAttribute(
                    "error",
                    "Password must contain 8+ characters, uppercase, lowercase, number and special symbol");

            return "register";
        }

        // PASSWORD VALIDATION ENDS HERE

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()));

        userService.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user =
                userService.findByUsername(username);

        if(user != null &&
        		   passwordEncoder.matches(
        		           password,
        		           user.getPassword())) {

        	    session.setAttribute("user", username);

        	    mailService.sendLoginAlert(
        	            user.getEmail(),
        	            user.getUsername());

        	    return "redirect:/";
        	}

        model.addAttribute(
                "error",
                "Invalid Username or Password");

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        if(!passwordEncoder.matches(
                oldPassword,
                user.getPassword())) {

            model.addAttribute(
                    "error",
                    "Current password is incorrect");

            return "change-password";
        }

        user.setPassword(
                passwordEncoder.encode(
                        newPassword));

        userService.save(user);

        model.addAttribute(
                "success",
                "Password changed successfully");

        return "change-password";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }
    @PostMapping("/send-otp")
    public String sendOtp(
            @RequestParam String email,
            HttpSession session,
            Model model) {

        User user =
                userService.findByEmail(email);

        if(user == null) {

            model.addAttribute(
                    "error",
                    "Email not registered");

            return "forgot-password";
        }

        String otp =
                String.valueOf(
                        (int)(Math.random()*900000)+100000);

        session.setAttribute("otp", otp);
        session.setAttribute("email", email);

        mailService.sendOtp(email, otp);

        return "verify-otp";
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model) {

        String savedOtp =
                (String) session.getAttribute("otp");

        if(savedOtp != null &&
           savedOtp.equals(otp)) {

            return "reset-password";
        }

        model.addAttribute(
                "error",
                "Invalid OTP");

        return "verify-otp";
    }
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String password,
            HttpSession session) {

        String email =
                (String) session.getAttribute("email");

        User user =
                userService.findByEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        password));

        userService.save(user);

        session.removeAttribute("otp");
        session.removeAttribute("email");

        return "redirect:/login";
    }
    @GetMapping("/profile")
    public String profilePage(HttpSession session,
                              Model model) {

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam String email,
            HttpSession session,
            Model model) {

        String username =
                (String) session.getAttribute("user");

        User user =
                userService.findByUsername(username);

        user.setEmail(email);

        userService.save(user);

        model.addAttribute(
                "success",
                "Profile updated successfully");

        model.addAttribute("user", user);

        return "profile";
    }
}