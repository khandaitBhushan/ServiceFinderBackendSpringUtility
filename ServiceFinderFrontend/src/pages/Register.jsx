import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import { api } from '../utils/api';

const Register = () => {
  const { registerAndLogin } = useAuth();
  const navigate = useNavigate();
  const [role, setRole] = useState('USER');
  const [error, setError] = useState('');
  const [categories, setCategories] = useState([]);

  // Basic Form
  const [formData, setFormData] = useState({
    name: '', email: '', password: '', phone: '', address: ''
  });

  // Provider Form
  const [providerData, setProviderData] = useState({
    companyName: '', description: '', experienceYears: '', 
    latitude: '', longitude: '', hourlyRate: '', minServicePrice: '', pricingDescription: '', serviceCategory: null
  });

  useEffect(() => {
    if (role === 'PROVIDER') {
      api.provider.getCategories()
        .then(data => {
          if (Array.isArray(data)) setCategories(data);
        })
        .catch(err => {
          console.log("Failed to fetch categories. Is backend running?", err);
          setError("Warning: Could not load categories. Ensure Spring Boot backend is running on 8080.");
        });
      
      // Auto fetch location logic (Requirement 6: button which use device current location and auto update)
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition((pos) => {
          setProviderData(prev => ({
            ...prev,
            latitude: pos.coords.latitude,
            longitude: pos.coords.longitude
          }));
        }, (err) => console.log("Location access denied", err));
      }
    }
  }, [role]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const registeredUser = await registerAndLogin({ ...formData, role });
      
      if (role === 'PROVIDER') {
        const pData = {
          ...providerData,
          location: formData.address, // share address
          serviceCategory: { id: parseInt(providerData.serviceCategory) }
        };
        await api.provider.register(pData);
        navigate('/provider-dashboard');
      } else {
        navigate('/user-dashboard');
      }
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="auth-container card">
      <h2 className="text-center">Register</h2>
      {error && <div className="text-red mb-4 text-center">{error}</div>}
      <form onSubmit={handleSubmit} className="flex-col">
        <label>Account Type:</label>
        <select value={role} onChange={e => setRole(e.target.value)}>
          <option value="USER">Standard User</option>
          <option value="PROVIDER">Service Provider</option>
        </select>

        <input type="text" placeholder="Full Name" required 
          value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
        <input type="email" placeholder="Email" required 
          value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})} />
        <input type="password" placeholder="Password" required 
          value={formData.password} onChange={e => setFormData({...formData, password: e.target.value})} />
        <input type="text" placeholder="Phone" required 
          value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})} />
        <input type="text" placeholder="Address" required 
          value={formData.address} onChange={e => setFormData({...formData, address: e.target.value})} />

        {role === 'PROVIDER' && (
          <div className="card" style={{ marginTop: '0', marginBottom: '15px' }}>
            <h4 className="mb-4 text-blue">Provider Details</h4>
            
            <label className="text-sm text-gray">Company / Business Name *</label>
            <input type="text" placeholder="e.g. Raj Electrical Services" required 
              value={providerData.companyName} onChange={e => setProviderData({...providerData, companyName: e.target.value})} />
            
            <label className="text-sm text-gray">Primary Category *</label>
            <select required value={providerData.serviceCategory || ''} onChange={e => setProviderData({...providerData, serviceCategory: e.target.value})}>
              <option value="" disabled>Select Primary Category</option>
              {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>

            <label className="text-sm text-gray">About your service (Desc) *</label>
            <textarea placeholder="e.g. We provide fast and reliable..." required 
              value={providerData.description} onChange={e => setProviderData({...providerData, description: e.target.value})} />
            
            <div className="grid grid-cols-2 gap-2">
              <div>
                <label className="text-sm text-gray">Years of Experience *</label>
                <input type="number" placeholder="Exp. Years" min="0" required 
                  value={providerData.experienceYears} onChange={e => setProviderData({...providerData, experienceYears: e.target.value})} />
              </div>
              <div>
                <label className="text-sm text-gray">Hourly Rate (₹) *</label>
                <input type="number" placeholder="Hourly Rate (₹)" required 
                  value={providerData.hourlyRate} onChange={e => setProviderData({...providerData, hourlyRate: e.target.value})} />
              </div>
            </div>

            <div>
              <label className="text-sm text-gray">Minimum Service Price (₹) *</label>
              <input type="number" placeholder="Min Service Price (₹)" required 
                value={providerData.minServicePrice} onChange={e => setProviderData({...providerData, minServicePrice: e.target.value})} />
            </div>
            
            <div>
              <label className="text-sm text-gray">Pricing Details (optional)</label>
              <input type="text" placeholder="e.g. Free inspection for first time" 
                value={providerData.pricingDescription} onChange={e => setProviderData({...providerData, pricingDescription: e.target.value})} />
            </div>
                
            <label className="text-sm text-gray">Target Location Coordinates (Auto-filled on Allow)</label>
            <div className="grid grid-cols-2 gap-2 mt-2">
              <input type="number" step="any" placeholder="Latitude" 
                value={providerData.latitude} onChange={e => setProviderData({...providerData, latitude: parseFloat(e.target.value)})} />
              <input type="number" step="any" placeholder="Longitude" 
                value={providerData.longitude} onChange={e => setProviderData({...providerData, longitude: parseFloat(e.target.value)})} />
            </div>
          </div>
        )}

        <button type="submit" className="w-full mt-4">Register</button>
      </form>
      <p className="text-center mt-4">
        Already have an account? <Link to="/login" className="text-blue">Login</Link>
      </p>
    </div>
  );
};

export default Register;
