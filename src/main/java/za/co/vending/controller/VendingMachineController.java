package za.co.vending.controller;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.co.vending.model.VendingItemDto;
import za.co.vending.model.VendingResponse;
import za.co.vending.utils.HttpUtil;

import java.net.URI;

@Slf4j
@Controller
public class VendingMachineController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.url}")
    private String apiUrl;
    @Autowired
    private HttpUtil httpUtil;

    // Displays the list of available vending items
    @GetMapping({"/","/items"})
    public String getItems(Model model) {
        String url = String.format("%s", apiUrl);
        ResponseEntity<VendingResponse> response = this.restTemplate.getForEntity(url, VendingResponse.class);
        model.addAttribute("items", response.getBody().getItems());
        return "vending-machine";
    }

    // Display the contents of the shopping cart and total price
    @GetMapping("/cart")
    public String viewCart(Model model) {
        String url = String.format("%s/cart", apiUrl);
        ResponseEntity<VendingResponse> response = this.restTemplate.getForEntity(url, VendingResponse.class);
        model.addAttribute("cart", response.getBody().getCart());
        model.addAttribute("totalPrice", response.getBody().getTotalPrice());
        return "cart";
    }

    // Displays the add-item form
    @GetMapping("/add-item")
    public ModelAndView addItem(Model model) {
        log.info("get add item");
        model.addAttribute("vendingItem", new VendingItemDto());
        ModelAndView mdv = new ModelAndView();
        mdv.setViewName("add-item");
        return mdv;
    }

    // Adds the new item to the vending machine OR updates the existing item in the vending machine
    @PostMapping("/add-item")
    public String saveItem(@ModelAttribute("vendingItem") VendingItemDto vendingItem, Model model) {
        log.info("post add item");
        try {
            String url = String.format("%s/add-item", apiUrl);

            log.info("itemDto ==> " + new Gson().toJson(vendingItem));
            HttpEntity<VendingItemDto> requestEntity = new HttpEntity<>(vendingItem, httpUtil.buildRequestHeaders());
            ResponseEntity<VendingResponse> responseEntity = restTemplate.postForEntity(new URI(url), requestEntity,
                    VendingResponse.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            log.info("status code => " + statusCode);
            log.info("api response =>" + new Gson().toJson(responseEntity.getBody()));
        } catch (Exception e) {
            log.error("error adding item [{}] to vending machine", vendingItem.getName());
        }
        return "redirect:/";
    }

    // Add selected item to the shopping cart
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam String itemName, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        try {
            String url = String.format("%s/add-to-cart", apiUrl);
            VendingItemDto itemDto = VendingItemDto.builder().name(itemName).quantity(quantity).build();

            log.info("itemDto ==> " + new Gson().toJson(itemDto));
            HttpEntity<VendingItemDto> requestEntity = new HttpEntity<>(itemDto, httpUtil.buildRequestHeaders());
            ResponseEntity<VendingResponse> responseEntity = restTemplate.postForEntity(new URI(url), requestEntity,
                    VendingResponse.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            log.info("status code => " + statusCode);
            log.info("api response =>" + new Gson().toJson(responseEntity.getBody()));
        } catch (Exception e) {
            log.error("error adding item [{}] to cart", itemName);
        }
        return "redirect:/";
    }

    // Calculate and display the total price before checkout
    @PostMapping("/checkout")
    public String checkout(Model model) {
        String url = String.format("%s/checkout", apiUrl);
        ResponseEntity<VendingResponse> response = this.restTemplate.getForEntity(url, VendingResponse.class);
        model.addAttribute("totalPrice", response.getBody().getTotalPrice());
        return "checkout";
    }

    // Accept payment, calculate change or amount due, and show result
    @PostMapping("/pay")
    public String pay(@RequestParam double amount, Model model) {
        try {
            String url = String.format("%s/pay", apiUrl);
            VendingItemDto itemDto = VendingItemDto.builder().amount(amount).build();

            log.info("itemDto ==> " + new Gson().toJson(itemDto));
            HttpEntity<VendingItemDto> requestEntity = new HttpEntity<>(itemDto, httpUtil.buildRequestHeaders());
            ResponseEntity<VendingResponse> responseEntity = restTemplate.postForEntity(new URI(url), requestEntity,
                    VendingResponse.class);
            HttpStatus statusCode = responseEntity.getStatusCode();
            log.info("status code => " + statusCode);
            log.info("api response =>" + new Gson().toJson(responseEntity.getBody()));
            model.addAttribute("change", responseEntity.getBody().getChange());
        } catch (Exception e) {
            log.error("error paying amount [{}]", amount);
        }
        return "payment-result";
    }

}

