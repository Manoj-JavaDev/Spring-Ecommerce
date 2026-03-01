<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Techouts Store</title>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=DM+Sans:wght@300;400;500;600&family=DM+Mono:wght@400&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/index2.css" rel="stylesheet" />
</head>

<body>

<!-- ================= HEADER ================= -->
<header>

    <div class="logo">Techouts</div>

    <nav>
        <a href="products">Home</a>
        <!-- Click-stable Categories Dropdown -->
        <div class="dropdown" id="catDropdown">
            <span onclick="toggleDropdown()">Categories</span>
            <div class="dropdown-content" id="catMenu">
                <a href="products?category=Mobiles">&#128241; Mobiles</a>
                <a href="products?category=Footwear">&#128608; Foot Wear</a>
                <a href="products?category=Books">&#128218; Books</a>
                <a href="products?category=Accessory">&#127914; Accessories</a>
            </div>
        </div>

    </nav>

      <!-- ===== SEARCH BAR ===== -->
        <div class="search-wrapper">
            <form class="search-form" action="products" method="get" onsubmit="return validateSearch()">
                <span class="search-icon">&#128269;</span>
                <input
                    type="text"
                    id="searchInput"
                    name="search"
                    class="search-input"
                    placeholder="Search products..."
                    value="${param.search}"
                    autocomplete="off"
                />
                <button type="submit" class="search-btn">Search</button>
                <c:if test="${not empty param.search}">
                    <a href="products" class="search-clear" title="Clear search">&#10005;</a>
                </c:if>
            </form>
            <!-- Live suggestions box -->
            <div class="search-suggestions" id="searchSuggestions"></div>
        </div>

    <c:choose>
        <c:when test="${pageContext.request.userPrincipal == null}">
            <a href="register">
                <button class="btn login-btn">Register</button>
            </a>
            <a href="login">
                <button class="btn login-btn">Login</button>
            </a>
        </c:when>
        <c:otherwise>
            <span class="welcome-text">
                Welcome, ${user.name}
            </span>
            <a href="profile">
                <button class="btn login-btn">Profile</button>
            </a>
             <a href="orders/myorders">
                <button class="btn login-btn">MyOrders</button>
             </a>
            <a href="logout">
                <button class="btn logout-btn">Logout</button>
            </a>
            <div class="right-section">
                <a href="cart/view">
                    <button class="btn logout-btn">&#128722; Cart</button>
                </a>
            </div>
        </c:otherwise>
    </c:choose>

</header>


<!-- ================= MAIN ================= -->
<main>
    <h2>Our Products</h2>
    <br>
    <div class="products">
        <c:forEach var="item" items="${products}">
            <div class="product-card">

                <!-- Make Image Clickable -->
                <a href="${pageContext.request.contextPath}/productDetails/${item.id}">
                    <img src="${item.imageUrl}" alt="${item.name}">
                </a>

                <!-- Make Name Clickable -->
                <h3>
                    <a href="${pageContext.request.contextPath}/productDetails/${item.id}"
                       style="text-decoration:none; color:black;">
                        ${item.name}
                    </a>
                </h3>

                <p>${item.title}</p><br>
                <p><b>&#8377;${item.price}</b></p>

                <form action="${pageContext.request.contextPath}/cart/add/${item.id}" method="post">
                    <button type="submit" class="btn">Add to Cart</button>
                </form>

            </div>
        </c:forEach>
    </div>
</main>


<!-- ================= FOOTER ================= -->
<footer>
    &#169; 2026 Techouts E-Commerce | All Rights Reserved
</footer>


<!-- ================= DROPDOWN JS ================= -->
<script>
    function toggleDropdown() {
        var menu = document.getElementById('catMenu');
        var span = document.querySelector('#catDropdown > span');
        var isOpen = menu.classList.contains('open');

        // Close all dropdowns first
        document.querySelectorAll('.dropdown-content').forEach(function(el) {
            el.classList.remove('open');
        });
        document.querySelectorAll('.dropdown > span').forEach(function(el) {
            el.classList.remove('open');
        });

        // Toggle current
        if (!isOpen) {
            menu.classList.add('open');
            span.classList.add('open');
        }
    }

    // Close dropdown when clicking anywhere outside
    document.addEventListener('click', function(e) {
        if (!document.getElementById('catDropdown').contains(e.target)) {
            document.querySelectorAll('.dropdown-content').forEach(function(el) {
                el.classList.remove('open');
            });
            document.querySelectorAll('.dropdown > span').forEach(function(el) {
                el.classList.remove('open');
            });
        }
    });
</script>

</body>
</html>
