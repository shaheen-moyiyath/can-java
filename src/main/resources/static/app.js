// ====================================================================
// Canteen Ordering System - Frontend Client API & State Management
// ====================================================================

const BASE_URL = '/api';

// State
const state = {
    token: localStorage.getItem('canteen_token') || null,
    user: JSON.parse(localStorage.getItem('canteen_user') || 'null'),
    menu: [],
    cart: [],
    orders: [],
    tallies: []
};

// Generic API Request Helper matching the document's design
async function apiRequest(endpoint, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json'
    };
    if (state.token) {
        headers['Authorization'] = `Bearer ${state.token}`;
    }

    const config = {
        method,
        headers
    };
    if (body) {
        config.body = JSON.stringify(body);
    }

    try {
        const res = await fetch(`${BASE_URL}${endpoint}`, config);
        const data = await res.json();
        if (!res.ok) {
            throw new Error(data.message || `Request failed with status ${res.status}`);
        }
        return data;
    } catch (err) {
        console.error(`API Error on ${method} ${endpoint}:`, err);
        throw err;
    }
}

// Authentication Functions
async function loginUser(username, password) {
    const res = await apiRequest('/auth/login', 'POST', { username, password });
    if (res.success && res.data) {
        state.token = res.data.token;
        state.user = {
            id: res.data.user_id || res.data.userId,
            username: res.data.username,
            fullName: res.data.full_name || res.data.fullName,
            role: res.data.role
        };
        localStorage.setItem('canteen_token', state.token);
        localStorage.setItem('canteen_user', JSON.stringify(state.user));
        return state.user;
    }
    throw new Error(res.message || 'Login failed');
}

async function registerUser(username, password, fullName, email) {
    const res = await apiRequest('/auth/register', 'POST', {
        username,
        password,
        fullName,
        email,
        role: 'STUDENT'
    });
    if (res.success && res.data) {
        state.token = res.data.token;
        state.user = {
            id: res.data.user_id || res.data.userId,
            username: res.data.username,
            fullName: res.data.full_name || res.data.fullName,
            role: res.data.role
        };
        localStorage.setItem('canteen_token', state.token);
        localStorage.setItem('canteen_user', JSON.stringify(state.user));
        return state.user;
    }
    throw new Error(res.message || 'Registration failed');
}

function logoutUser() {
    state.token = null;
    state.user = null;
    localStorage.removeItem('canteen_token');
    localStorage.removeItem('canteen_user');
    window.location.reload();
}

// Menu Management
async function fetchMenu(all = false) {
    const endpoint = all ? '/menu/all' : '/menu';
    const res = await apiRequest(endpoint);
    state.menu = res.data || [];
    return state.menu;
}

// Cart Operations (Student)
function addToCart(item) {
    const existing = state.cart.find(i => i.item_id === (item.item_id || item.id));
    if (existing) {
        existing.quantity += 1;
    } else {
        state.cart.push({
            item_id: item.item_id || item.id,
            name: item.name,
            price: item.price,
            quantity: 1
        });
    }
    updateCartUI();
}

function updateCartQuantity(itemId, delta) {
    const item = state.cart.find(i => i.item_id === itemId);
    if (!item) return;

    item.quantity += delta;
    if (item.quantity <= 0) {
        state.cart = state.cart.filter(i => i.item_id !== itemId);
    }
    updateCartUI();
}

function clearCart() {
    state.cart = [];
    updateCartUI();
}

// Order Operations
async function submitOrder() {
    if (state.cart.length === 0) {
        alert('Your cart is empty!');
        return;
    }
    if (!state.token) {
        openAuthModal('login');
        return;
    }

    try {
        const payload = {
            items: state.cart.map(i => ({ item_id: i.item_id, quantity: i.quantity }))
        };
        const res = await apiRequest('/orders', 'POST', payload);
        if (res.success) {
            const orderId = res.data.id || res.data.order_id;
            alert(`Order #${orderId} placed successfully! Thank you.`);
            clearCart();
            if (typeof fetchOrderHistory === 'function') {
                fetchOrderHistory();
            }
        }
    } catch (err) {
        alert(`Order error: ${err.message}`);
    }
}

async function fetchOrderHistory() {
    if (!state.token) return [];
    try {
        const res = await apiRequest('/orders/my-history');
        state.orders = res.data || [];
        renderOrderHistory();
        return state.orders;
    } catch (err) {
        console.error('Failed to fetch order history:', err);
        return [];
    }
}

// Admin Operations
async function fetchKitchenTallies(date = null) {
    const endpoint = date ? `/admin/dashboard/live-counts?date=${date}` : '/admin/dashboard/live-counts';
    const res = await apiRequest(endpoint);
    state.tallies = res.data || [];
    return state.tallies;
}

async function fetchAdminOrders(date = null) {
    const endpoint = date ? `/admin/orders?date=${date}` : '/admin/orders';
    const res = await apiRequest(endpoint);
    return res.data || [];
}

async function updateOrderStatus(orderId, newStatus) {
    const res = await apiRequest(`/admin/orders/${orderId}/status`, 'PUT', { status: newStatus });
    return res.data;
}

async function fetchDailyReport(date = null) {
    const endpoint = date ? `/admin/reports/daily?date=${date}` : '/admin/reports/daily';
    const res = await apiRequest(endpoint);
    return res.data;
}

async function createMenuItem(itemData) {
    const res = await apiRequest('/menu', 'POST', itemData);
    return res.data;
}

async function updateMenuItem(id, itemData) {
    const res = await apiRequest(`/menu/${id}`, 'PUT', itemData);
    return res.data;
}

async function toggleMenuItemAvailability(id, isAvailable) {
    const res = await apiRequest(`/menu/${id}/toggle-availability?available=${isAvailable}`, 'PATCH');
    return res.data;
}

async function deleteMenuItem(id) {
    const res = await apiRequest(`/menu/${id}`, 'DELETE');
    return res;
}
