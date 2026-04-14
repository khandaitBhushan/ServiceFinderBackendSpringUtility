const API_BASE_URL = 'http://localhost:8080';

export const request = async (endpoint, options = {}) => {
  const token = localStorage.getItem('token');
  
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const url = `${API_BASE_URL}${endpoint}`;
  console.log(`API Call: ${options.method || 'GET'} ${url}`);

  const response = await fetch(url, {
    ...options,
    headers,
  });

  // Handle No Content (204)
  if (response.status === 204) return null;

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(data.message || data.error || 'API Request Failed');
  }

  return data;
};

// Endpoints definitions mapped from backend
export const api = {
  auth: {
    login: (credentials) => request('/auth/login', { method: 'POST', body: JSON.stringify(credentials) }),
    registerUser: (data) => request('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
  },
  user: {
    me: () => request('/users/me'),
    update: (data) => request('/users/update', { method: 'PUT', body: JSON.stringify(data) })
  },
  provider: {
    register: (data) => request('/providers/register', { method: 'POST', body: JSON.stringify(data) }),
    getAll: () => request('/providers'),
    search: (category, location) => request(`/providers/search?category=${category || ''}&location=${location || ''}`),
    nearbyCategory: (category, lat, lng, dist) => {
      let query = `?latitude=${lat}&longitude=${lng}`;
      if (category) query += `&category=${category}`;
      if (dist) query += `&maxDistance=${dist}`;
      return request(`/providers/nearby/category${query}`);
    },
    nearby: (lat, lng, dist) => {
      let query = `?latitude=${lat}&longitude=${lng}`;
      if (dist) query += `&maxDistance=${dist}`;
      return request(`/providers/nearby${query}`);
    },
    getCategories: () => request('/providers/categories')
  },
  providerServices: {
    add: (data) => request('/provider-services', { method: 'POST', body: JSON.stringify(data) }),
    getMy: () => request('/provider-services/my-services'),
    getByProvider: (id) => request(`/provider-services/provider/${id}`)
  },
  bookings: {
    create: (providerId, date) => request(`/bookings?providerId=${providerId}&dateTime=${date}`, { method: 'POST' }),
    createWithService: (payload) => request(`/bookings/with-service?providerId=${payload.providerId}&serviceId=${payload.serviceId}&dateTime=${payload.dateTime}&userNotes=${payload.notes}&serviceAddress=${payload.address}`, { method: 'POST' }),
    getUserBookings: () => request('/bookings/user'),
    getProviderBookings: () => request('/bookings/provider'),
    updateStatus: (id, status) => request(`/bookings/${id}/status?status=${status}`, { method: 'PUT' }),
    cancel: (id) => request(`/bookings/${id}/cancel`, { method: 'PUT' })
  }
};
