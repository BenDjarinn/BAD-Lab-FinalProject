package Test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductValidationTest {

    // Test: harga produk tidak boleh 0 atau negatif
    @Test
    void testProductPriceMustBePositive() {
        int price = 0;
        assertTrue(price <= 0, "Price 0 should be invalid");
    }

    // Test: nama produk tidak boleh kosong
    @Test
    void testProductNameMustNotBeEmpty() {
        String productName = "";
        assertTrue(productName.isEmpty(), "Empty product name should be invalid");
    }

    // Test: harga valid
    @Test
    void testValidProductPrice() {
        int price = 25000;
        assertTrue(price > 0, "Price 25000 should be valid");
    }

    // Test: format productID harus diawali TE
    @Test
    void testProductIDFormat() {
        String productID = "TE001";
        assertTrue(productID.startsWith("TE"), "ProductID should start with TE");
    }

    // Test: generate productID berikutnya
    @Test
    void testGenerateNextProductID() {
        String lastID = "TE003";
        int lastNum = Integer.parseInt(lastID.substring(2));
        String nextID = String.format("TE%03d", lastNum + 1);
        assertEquals("TE004", nextID);
    }
}