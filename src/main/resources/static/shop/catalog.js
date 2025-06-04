async function loadProducts() {
    try {
        const response = await fetch('/api/products');
        const products = await response.json();

        const container = document.getElementById('products');
        products.forEach(product => {
            container.innerHTML += `
                <div class="product">
                    <h3>${product.name}</h3>
                    <p>Цена: ${product.price} руб.</p>
                    <button onclick="addToCart(${product.id})">В корзину</button>
                </div>
            `;
        });
    } catch (error) {
        console.error('Ошибка загрузки товаров:', error);
    }
}

function addToCart(productId) {
    fetch(`/api/cart/add?productId=${productId}`, { method: 'POST' })
        .then(response => alert('Товар добавлен в корзину!'))
        .catch(error => console.error('Ошибка:', error));
}


loadProducts();