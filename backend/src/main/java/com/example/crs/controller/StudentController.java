package com.example.crs.controller;
import com.example.crs.model.Student; // import Student 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "http://localhost:5173") // Stay consistent with CourseController for Vue
@RestController
@RequestMapping("/api")
public class StudentController {

    // Task 1: Simulate "in-memory" Student "Database"
    private static final Map<String, Student> studentDatabase = new ConcurrentHashMap<>();

    // generate a test account 
    static {
        Student defaultStudent = new Student();
        defaultStudent.setStudentId("1001");
        defaultStudent.setPassword("123456");
        defaultStudent.setName("Test Student");
        defaultStudent.setEmail("test@school.edu");
        studentDatabase.put("1001", defaultStudent);
    }

    // Task 3: Login validated against the in-memory map
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String studentId = loginRequest.get("studentId");
        String password = loginRequest.get("password");

        // 1. input must not be empty -> 400
        if (studentId == null || studentId.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Student ID and Password cannot be empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // 2. look up the student
        Student student = studentDatabase.get(studentId);

        // 3. validate -> 200 with real id and name
        if (student != null && student.getPassword().equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", student.getStudentId());
            response.put("name", student.getName());
            return ResponseEntity.ok(response);
        }

        // 4. invalid -> 401
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid Student ID or Password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // Task 2: Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student newStudent) {
        // validate required fields -> 400
        if (newStudent.getStudentId() == null || newStudent.getStudentId().trim().isEmpty() ||
            newStudent.getPassword() == null || newStudent.getPassword().trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Student ID and Password cannot be empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // 1 and 2. ID already exists -> 409 Conflict
        if (studentDatabase.containsKey(newStudent.getStudentId())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Registration failed: Student ID already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // 3. unique ID -> save to the map
        studentDatabase.put(newStudent.getStudentId(), newStudent);

        // 4. 201 Created
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Registration successful!");
        return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
    }
}
