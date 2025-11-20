package com.example.foodwaste.data.product

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class ProductInfo(
    val barcode: String,
    val name: String,
    val category: String,
    val defaultShelfDays: Long
)

@Serializable
private data class OffProductResponse(
    val status: Int? = null,
    val product: OffProduct? = null
)

@Serializable
private data class OffProduct(
    @SerialName("product_name") val productName: String? = null,
    @SerialName("categories_tags") val categoriesTags: List<String>? = null,
    @SerialName("brands") val brands: String? = null
)

object OpenFoodFactsApi {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun lookup(ean: String): ProductInfo? {
        val url = "https://world.openfoodfacts.org/api/v2/product/$ean.json"

        return runCatching {
            val resp: OffProductResponse = client.get(url).body()
            if (resp.status != 1 || resp.product == null) return null

            val p = resp.product
            val rawName = p.productName?.takeIf { it.isNotBlank() } ?: return null
            val tag = p.categoriesTags?.firstOrNull() ?: "en:other"

            val categoryPretty = tag.substringAfter(':')
                .replace('-', ' ')
                .replaceFirstChar { it.uppercaseChar() }

            ProductInfo(
                barcode = ean,
                name = rawName,
                category = categoryPretty,
                defaultShelfDays = ProductShelfLife.guessDays(categoryPretty)
            )
        }.getOrNull()
    }
}

object ProductShelfLife {
    fun guessDays(category: String): Long {
        val c = category.lowercase()
        return when {
            listOf("milk", "yoghurt", "yogurt", "dairy").any { it in c } -> 7
            listOf("meat", "chicken", "poultry", "fish").any { it in c } -> 3
            listOf("bread", "bakery").any { it in c } -> 3
            listOf("noodle", "pasta", "rice").any { it in c } -> 90
            listOf("snack", "chips", "biscuit", "cookie").any { it in c } -> 120
            listOf("frozen").any { it in c } -> 180
            else -> 14
        }
    }
}

object LocalProductDB {

    private val products: Map<String, ProductInfo> = listOf(
        // Dairy
        ProductInfo("8888000000001", "FairPrice Fresh Milk 1L", "Dairy", 7),
        ProductInfo("8888000000002", "Magnolia Fresh Milk 1L", "Dairy", 7),
        ProductInfo("8888000000003", "Marigold HL Milk 1L", "Dairy", 7),
        ProductInfo("8888000000004", "Farmhouse Fresh Milk 2L", "Dairy", 7),
        ProductInfo("8888000000005", "FairPrice UHT Milk 1L", "Dairy", 30),
        ProductInfo("8888000000006", "Dutch Lady Chocolate Milk 1L", "Dairy", 14),
        ProductInfo("8888000000007", "Yogurt Drink Original 500ml", "Dairy", 10),
        ProductInfo("8888000000008", "Meiji Plain Yogurt 470g", "Dairy", 10),

        // Eggs / Breakfast
        ProductInfo("8888000000010", "FairPrice Fresh Eggs 10s", "Eggs", 14),
        ProductInfo("8888000000011", "Egg Story Pasteurized Eggs 10s", "Eggs", 14),
        ProductInfo("8888000000012", "Sunny Side Omega Eggs 10s", "Eggs", 14),

        // Rice / Noodles / Bread
        ProductInfo("8888000000020", "FairPrice Jasmine Fragrant Rice 5kg", "Grains", 365),
        ProductInfo("8888000000021", "Royal Umbrella Thai Hom Mali Rice 5kg", "Grains", 365),
        ProductInfo("8888000000022", "SongHe Thai Fragrant Rice 5kg", "Grains", 365),
        ProductInfo("8888000000023", "San Remo Spaghetti 500g", "Pasta", 365),
        ProductInfo("8888000000024", "Koka Instant Noodles Original 5s", "Instant Noodles", 365),
        ProductInfo("8888000000025", "MAMA Tom Yum Instant Noodles 5s", "Instant Noodles", 365),
        ProductInfo("8888000000026", "Gardenia White Bread 400g", "Bakery", 3),
        ProductInfo("8888000000027", "Sunshine Wholemeal Bread 400g", "Bakery", 3),

        // Meat / Frozen
        ProductInfo("8888000000030", "FairPrice Frozen Chicken Wings 1kg", "Frozen Meat", 90),
        ProductInfo("8888000000031", "FairPrice Fresh Chicken Breast 500g", "Fresh Meat", 3),
        ProductInfo("8888000000032", "CP Chicken Nuggets 800g", "Frozen Meat", 120),
        ProductInfo("8888000000033", "FairPrice Frozen Mixed Vegetables 1kg", "Frozen Vegetables", 120),
        ProductInfo("8888000000034", "FairPrice Frozen French Fries 1kg", "Frozen Snacks", 120),

        // Vegetables / Fruit (with a shorter shelf life)
        ProductInfo("8888000000040", "FairPrice Fresh Broccoli 300g", "Vegetables", 5),
        ProductInfo("8888000000041", "FairPrice Fresh Kailan 300g", "Vegetables", 5),
        ProductInfo("8888000000042", "FairPrice Fresh Tomatoes 500g", "Vegetables", 5),
        ProductInfo("8888030023006", "Spring Onion 100g", "Vegetables", 5),
        ProductInfo("8888000000043", "FairPrice Fresh Bananas 1kg", "Fruits", 5),
        ProductInfo("8888000000044", "FairPrice Fresh Apples 1kg", "Fruits", 10),
        ProductInfo("8888000000045", "Sunkist Oranges 1kg", "Fruits", 10),

        // Seasoning / Soy sauce
        ProductInfo("8888000000050", "FairPrice Light Soy Sauce 640ml", "Condiments", 365),
        ProductInfo("8888000000051", "Kim Lan Superior Soy Sauce 600ml", "Condiments", 365),
        ProductInfo("8888000000052", "Lee Kum Kee Oyster Sauce 510g", "Condiments", 365),
        ProductInfo("8888000000053", "Kikkoman Soy Sauce 1L", "Condiments", 365),
        ProductInfo("8888000000054", "FairPrice Cooking Oil 2L", "Oil", 365),

        // Beverages / Mineral water
        ProductInfo("8888000000060", "Coca-Cola Original 1.5L", "Soft Drinks", 180),
        ProductInfo("8888000000061", "Sprite 1.5L", "Soft Drinks", 180),
        ProductInfo("8888000000062", "Heaven and Earth Green Tea 1.5L", "Tea Drinks", 180),
        ProductInfo("8888000000063", "Pokka Jasmine Green Tea 1.5L", "Tea Drinks", 180),
        ProductInfo("8888000000064", "FairPrice Drinking Water 1.5L", "Water", 365),
        ProductInfo("8888000000065", "Ice Mountain Mineral Water 1.5L", "Water", 365),

        //  Snacks
        ProductInfo("8888000000070", "Calbee Hot & Spicy Potato Chips 80g", "Snacks", 180),
        ProductInfo("8888000000071", "Jack n Jill Roller Coaster 60g", "Snacks", 180),
        ProductInfo("8888000000072", "Pocky Chocolate 40g", "Snacks", 180),
        ProductInfo("8888000000073", "KitKat 4 Finger 35g", "Snacks", 180),
        ProductInfo("8888000000074", "Julie’s Peanut Butter Sandwich 300g", "Snacks", 180),
        ProductInfo("8888000000075", "Meiji Hello Panda 260g", "Snacks", 180),

        // Tinned / Ready-to-eat
        ProductInfo("8888000000080", "Campbell’s Mushroom Soup 305g", "Canned Food", 365),
        ProductInfo("8888000000081", "Ayam Brand Sardines in Tomato 425g", "Canned Food", 365),
        ProductInfo("8888000000082", "FairPrice Baked Beans 425g", "Canned Food", 365),
        ProductInfo("8888000000083", "Hosen Longan in Syrup 565g", "Canned Food", 365),
        ProductInfo("8888000000084", "FairPrice Luncheon Meat 340g", "Canned Food", 365),

        // Breakfast cereals
        ProductInfo("8888000000090", "Nestle Koko Krunch 330g", "Cereal", 180),
        ProductInfo("8888000000091", "Kellogg's Corn Flakes 500g", "Cereal", 180),
        ProductInfo("8888000000092", "Quaker Instant Oatmeal 800g", "Cereal", 365),

        // Dairy-based desserts / Ice cream
        ProductInfo("8888000000100", "Walls Vanilla Ice Cream 1.5L", "Ice Cream", 180),
        ProductInfo("8888000000101", "Ben & Jerry’s Chocolate Fudge Brownie 473ml", "Ice Cream", 180),
        ProductInfo("8888000000102", "Haagen-Dazs Strawberry 473ml", "Ice Cream", 180),

        // Seasoning sauce / Chilli / Tomato ketchup
        ProductInfo("8888000000110", "Heinz Tomato Ketchup 320g", "Condiments", 365),
        ProductInfo("8888000000111", "Thai Chilli Sauce 725ml", "Condiments", 365),
        ProductInfo("8888000000112", "Lingham Chili Sauce 340ml", "Condiments", 365),

        // Chilled cooked foods
        ProductInfo("8888000000120", "FairPrice Chicken Ham 200g", "Chilled Food", 7),
        ProductInfo("8888000000121", "FairPrice Cheese Slices 12s", "Chilled Food", 14),
        ProductInfo("8888000000122", "CP Ready Meal Fried Rice 350g", "Chilled Food", 7),

        // Miscellaneous
        ProductInfo("8888000000130", "FairPrice Tofu Soft 300g", "Chilled Food", 5),
        ProductInfo("8888000000131", "FairPrice Firm Tofu 300g", "Chilled Food", 5),
        ProductInfo("8888000000132", "FairPrice UHT Chocolate Milk 1L", "Dairy", 30),
        ProductInfo("8888000000133", "Milo 3-in-1 18s", "Beverages", 180),
        ProductInfo("8888000000134", "Nescafe 3-in-1 Coffee 28s", "Beverages", 180)
    ).associateBy { it.barcode }

    fun find(ean: String): ProductInfo? = products[ean]
}

object ProductLookup {

    suspend fun lookupProduct(ean: String): ProductInfo? {
        val off = OpenFoodFactsApi.lookup(ean)
        if (off != null) return off

        val local = LocalProductDB.find(ean)
        if (local != null) return local

        return null
    }
}
