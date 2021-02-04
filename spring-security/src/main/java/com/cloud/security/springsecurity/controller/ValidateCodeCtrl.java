package com.cloud.security.springsecurity.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.security.springsecurity.config.properties.SecurityProperties;
import com.cloud.security.springsecurity.constants.SecurityConst;
import com.cloud.security.springsecurity.security.validatecode.model.ImageCode;
import com.cloud.security.springsecurity.security.validatecode.model.ValidateCode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Api(tags = "验证码模块")
@RestController
public class ValidateCodeCtrl {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    SecurityProperties securityProperties;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode(request);
        String imageCodeKey = UUID.randomUUID().toString();

        ValidateCode validateCode = new ValidateCode();
        validateCode.setCode(imageCode.getCode());
        validateCode.setExpireTime(imageCode.getExpireTime());
        redisTemplate.opsForHash().put(SecurityConst.IMAGE_CODE_KEY, imageCodeKey,JSONObject.toJSONString(validateCode));
        response.setHeader(SecurityConst.IMAGE_CODE_KEY,imageCodeKey);
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    /**
     * 创建图片验证码
     * @param request
     * @return
     */
    private ImageCode createImageCode(HttpServletRequest request) {
        int width = securityProperties.getCode().getImage().getWidth();
        int height = securityProperties.getCode().getImage().getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        g.dispose();

        return new ImageCode(image, sRand, securityProperties.getCode().getImage().getExpireIn());
    }

    /**
     * 生成随机背景条纹
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
