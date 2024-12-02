package Bikes;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class BikeController extends HttpServlet {
    private BikeService bikeService = new BikeService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form data
        int id = Integer.parseInt(request.getParameter("id"));
        String model = request.getParameter("model");
        String brand = request.getParameter("brand");
        String condition = request.getParameter("condition");
        String color = request.getParameter("color");
        boolean isAvailable = Boolean.parseBoolean(request.getParameter("isAvailable"));

        // Save images
        String uploadPath = getServletContext().getRealPath("/css/images");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        List<String> imagePaths = new ArrayList<>();
        for (Part part : request.getParts()) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String fileName = extractFileName(part);
                String filePath = uploadPath + File.separator + fileName;
                part.write(filePath);
                imagePaths.add("css/images/" + fileName);
            }
        }

        // Add bike
        Bike bike = new Bike(id, model, brand, condition, color, isAvailable, imagePaths);
        bikeService.addBike(bike);

        response.getWriter().write("Bike added successfully!");
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String content : contentDisp.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return null;
    }
}
