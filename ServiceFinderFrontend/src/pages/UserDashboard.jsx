import React, { useState, useEffect } from 'react';
import { api } from '../utils/api';
import { useAuth } from '../contexts/AuthContext';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
    iconUrl: icon, shadowUrl: iconShadow, iconAnchor: [12, 41]
});
L.Marker.prototype.options.icon = DefaultIcon;

const MapUpdater = ({ coords }) => {
  const map = useMap();
  useEffect(() => {
    if (coords && coords.lat && coords.lng) {
      map.setView([coords.lat, coords.lng], 13);
    }
  }, [coords, map]);
  return null;
};

const UserDashboard = () => {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('search'); // 'search', 'bookings', 'profile'
  
  const [location, setLocation] = useState({ lat: 21.1458, lng: 79.0882 });
  const [radius, setRadius] = useState(10);
  const [providers, setProviders] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  
  // Universal search filters
  const [citySearchText, setCitySearchText] = useState('');
  
  // Bookings state
  const [myBookings, setMyBookings] = useState([]);
  
  // Modals for Booking
  const [selectedProvider, setSelectedProvider] = useState(null);
  const [providerServices, setProviderServices] = useState([]);
  const [bookingForm, setBookingForm] = useState({ date: '', notes: '', address: '', serviceId: '' });

  // Profile Form
  const [profileForm, setProfileForm] = useState({
    name: user?.name || '', email: user?.email || '', phone: user?.phone || '', address: user?.address || ''
  });

  const fetchProviders = async () => {
    if (citySearchText.trim()) {
      // Endpoint: Text-based search without forcing geocoordinates
      const data = await api.provider.search(selectedCategory || '', citySearchText).catch(() => []);
      setProviders(data);
    } else {
      // Endpoint: Nearby radius search
      if (selectedCategory) {
        const data = await api.provider.nearbyCategory(selectedCategory, location.lat, location.lng, radius).catch(() => []);
        setProviders(data);
      } else {
        const data = await api.provider.nearby(location.lat, location.lng, radius).catch(() => []);
        setProviders(data);
      }
    }
  };

  const loadAllProviders = async () => {
    // Endpoint: Get ALL universally
    const data = await api.provider.getAll().catch(() => []);
    setProviders(data);
  };

  useEffect(() => {
    if (activeTab === 'search') fetchProviders();
    if (activeTab === 'bookings') loadBookings();
  }, [location, selectedCategory, radius, activeTab]);

  const useDeviceLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (pos) => setLocation({ lat: pos.coords.latitude, lng: pos.coords.longitude }),
        () => alert("Please allow location access.")
      );
    }
  };

  const openBookingModal = async (providerObj) => {
    setSelectedProvider(providerObj);
    setBookingForm({ date: '', notes: '', address: '', serviceId: '' });
    try {
      const pId = providerObj.provider ? providerObj.provider.id : providerObj.id;
      const services = await api.providerServices.getByProvider(pId);
      setProviderServices(services);
    } catch(err) {}
  };

  const submitBooking = async (e) => {
    e.preventDefault();
    try {
      const pId = selectedProvider.provider ? selectedProvider.provider.id : selectedProvider.id;
      await api.bookings.createWithService({
        providerId: pId,
        serviceId: bookingForm.serviceId,
        dateTime: bookingForm.date + ':00',
        notes: bookingForm.notes,
        address: bookingForm.address
      });
      alert("Booking successfully placed!");
      setSelectedProvider(null);
    } catch (err) { alert("Error booking: " + err.message); }
  };

  const loadBookings = async () => {
    const b = await api.bookings.getUserBookings().catch(() => []);
    setMyBookings(b);
  };

  const cancelBooking = async (id) => {
    try {
      if(!window.confirm("Are you sure you want to cancel this booking?")) return;
      await api.bookings.cancel(id);
      loadBookings();
    } catch(err) { alert("Failed to cancel."); }
  };

  const updateProfile = async (e) => {
    e.preventDefault();
    try {
      await api.user.update(profileForm);
      alert("Profile successfully updated!");
    } catch(err) { alert("Failed to update profile: " + err.message); }
  };

  return (
    <div className="w-full">
      <div className="flex gap-4 mb-4">
        <button className={activeTab === 'search' ? '' : 'text-gray'} style={{backgroundColor: activeTab === 'search' ? '#1E90FF': '#333'}} onClick={() => setActiveTab('search')}>Find Services</button>
        <button className={activeTab === 'bookings' ? '' : 'text-gray'} style={{backgroundColor: activeTab === 'bookings' ? '#1E90FF': '#333'}} onClick={() => setActiveTab('bookings')}>My Bookings</button>
        <button className={activeTab === 'profile' ? '' : 'text-gray'} style={{backgroundColor: activeTab === 'profile' ? '#1E90FF': '#333'}} onClick={() => setActiveTab('profile')}>My Profile</button>
      </div>

      {activeTab === 'profile' && (
        <div className="card p-4" style={{maxWidth: '500px', margin: '0 auto'}}>
          <h3>Update Profile</h3>
          <form onSubmit={updateProfile} className="flex-col">
            <label className="text-sm text-gray">Full Name</label>
            <input type="text" value={profileForm.name} onChange={e => setProfileForm({...profileForm, name: e.target.value})} />
            
            <label className="text-sm text-gray">Email Address (Read-only)</label>
            <input type="text" value={profileForm.email} readOnly style={{opacity: 0.7}} />
            
            <label className="text-sm text-gray">Phone Number</label>
            <input type="text" value={profileForm.phone} onChange={e => setProfileForm({...profileForm, phone: e.target.value})} />
            
            <label className="text-sm text-gray">Default Address</label>
            <input type="text" value={profileForm.address} onChange={e => setProfileForm({...profileForm, address: e.target.value})} />
            
            <button type="submit">Save Changes</button>
          </form>
        </div>
      )}

      {activeTab === 'bookings' && (
        <div className="card">
          <h3>Your Booking History</h3>
          {myBookings.length === 0 && <p className="text-gray">You haven't made any bookings yet!</p>}
          {myBookings.map(b => (
             <div key={b.id} className="p-4 mb-4" style={{border: '1px solid #333', borderRadius: '8px'}}>
               <h4 style={{margin: '0 0 5px 0'}}>{b.serviceProvider?.companyName} 
                 <span style={{float: 'right', fontSize: '12px'}} className={b.status === 'PENDING' ? 'text-blue' : b.status === 'CONFIRMED' ? 'text-green' : 'text-red'}>
                    {b.status}
                 </span>
               </h4>
               <p className="text-sm text-gray">Service: {b.service?.serviceName} (₹{b.service?.price})</p>
               <p className="text-sm">Scheduled Time: {new Date(b.date).toLocaleString()}</p>
               
               {['PENDING', 'CONFIRMED'].includes(b.status) && (
                 <button onClick={() => cancelBooking(b.id)} style={{backgroundColor: '#e74c3c', color: '#fff', marginTop: '10px'}} className="text-sm">Cancel Booking</button>
               )}
             </div>
          ))}
        </div>
      )}

      {activeTab === 'search' && (
        <>
          <div className="card grid md-grid-cols-2 gap-4">
            <div>
              <h4 className="mb-2">Advanced Filters</h4>
              <div className="flex gap-2 mb-2 items-center">
                <input type="number" step="any" value={location.lat} onChange={(e) => setLocation({...location, lat: parseFloat(e.target.value)})} placeholder="Lat" style={{marginBottom: 0}} />
                <input type="number" step="any" value={location.lng} onChange={(e) => setLocation({...location, lng: parseFloat(e.target.value)})} placeholder="Lng" style={{marginBottom: 0}} />
                <button onClick={useDeviceLocation}>📍 GPS</button>
              </div>

              <div className="grid grid-cols-2 gap-2">
                <select value={selectedCategory} onChange={(e) => setSelectedCategory(e.target.value)}>
                  <option value="">All Categories</option>
                  <option value="Electrician">Electricians</option>
                  <option value="Plumber">Plumbers</option>
                  <option value="Carpenter">Carpenters (Smiths)</option>
                  <option value="AC Repair">AC Repair</option>
                  <option value="Painter">Painters</option>
                </select>
                <input type="number" min="1" max="100" value={radius} onChange={(e) => setRadius(parseInt(e.target.value))} placeholder="Radius (km)"/>
              </div>

              <div className="flex gap-2 mt-2 items-center">
                <input type="text" placeholder="Or Search City Location (e.g. Nagpur)" value={citySearchText} onChange={(e) => setCitySearchText(e.target.value)} style={{marginBottom: 0}}/>
                <button onClick={fetchProviders}>Search</button>
                <button onClick={loadAllProviders} className="text-sm" style={{backgroundColor: '#333', color: '#fff'}}>See All Providers</button>
              </div>
            </div>

            <div className="flex-col justify-center">
              <h4>Quick Category Shortcuts</h4>
              <div className="flex gap-2 mb-2">
                <button style={{flex: 1}} onClick={() => setSelectedCategory('Electrician')}>⚡ Electricians</button>
                <button style={{flex: 1}} onClick={() => setSelectedCategory('Plumber')}>💧 Plumbers</button>
                <button style={{flex: 1}} onClick={() => setSelectedCategory('Carpenter')}>🪑 Carpenters</button>
              </div>
            </div>
          </div>

          <div className="grid md-grid-cols-2 gap-4">
            <div style={{ maxHeight: '600px', overflowY: 'auto' }}>
              <h3>Nearby Utilities ({providers.length})</h3>
              {providers.length === 0 && <p className="text-gray">No providers found in this area.</p>}
              
              {providers.map((p, idx) => {
                const provider = p.provider || p;
                const distance = p.distance != null ? p.distance : null;
                return (
                  <div key={provider.id || idx} className="card p-4">
                    <div className="flex justify-between items-center mb-2">
                      <h3 style={{margin: 0}}>{provider.companyName}</h3>
                      <span className="text-blue font-bold text-sm">
                        {distance !== null ? `${distance} km away` : provider.location}
                      </span>
                    </div>
                    <p className="text-sm text-gray">{provider.serviceCategory?.name || 'Service Provider'}</p>
                    <p className="text-sm">{provider.description}</p>
                    
                    <div className="flex justify-between items-center mt-4">
                      <span className="text-sm text-gray">{!provider.rating || provider.rating===0 ? 'No ratings' : `⭐ ${provider.rating}/5.0`}</span>
                      <button onClick={() => openBookingModal(p)}>View & Book</button>
                    </div>
                  </div>
                );
              })}
            </div>

            <div>
               <h3>Live Area Map</h3>
               <div style={{border: '2px solid #333', borderRadius: '12px', overflow: 'hidden'}}>
                 <MapContainer center={[location.lat, location.lng]} zoom={13} scrollWheelZoom={true}>
                   <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                   <MapUpdater coords={location} />
                   <Marker position={[location.lat, location.lng]}><Popup>Your Search Frame</Popup></Marker>
                   
                   {providers.map((p, idx) => {
                     const provider = p.provider || p;
                     if (!provider.latitude || !provider.longitude) return null;
                     return (
                       <Marker key={`p-${provider.id || idx}`} position={[provider.latitude, provider.longitude]}>
                         <Popup><strong style={{color: '#1a1a1a'}}>{provider.companyName}</strong></Popup>
                       </Marker>
                     );
                   })}
                 </MapContainer>
               </div>
            </div>
          </div>
        </>
      )}

      {selectedProvider && (
        <div style={{
          position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', 
          backgroundColor: 'rgba(0,0,0,0.8)', display: 'flex', alignItems: 'center', justifyContent: 'center',
          boxSizing: 'border-box', zIndex: 1000
        }}>
          <div className="card" style={{width: '90%', maxWidth: '500px', backgroundColor: '#151515'}}>
            <h3>Book {selectedProvider.provider?.companyName || selectedProvider.companyName}</h3>
            
            <form onSubmit={submitBooking} className="flex-col">
              <select required value={bookingForm.serviceId} onChange={e => setBookingForm({...bookingForm, serviceId: e.target.value})}>
                <option value="" disabled>{providerServices.length===0?'No active services':'Select a required service'}</option>
                {providerServices.map(s => (
                  <option key={s.id} value={s.id}>{s.serviceName} - ₹{s.price}</option>
                ))}
              </select>

              <input type="datetime-local" required value={bookingForm.date} 
                onChange={e => setBookingForm({...bookingForm, date: e.target.value})} />
              <input type="text" placeholder="Service Address (Your address)" required 
                value={bookingForm.address} onChange={e => setBookingForm({...bookingForm, address: e.target.value})} />
              <textarea placeholder="Any specific notes for the provider?" 
                value={bookingForm.notes} onChange={e => setBookingForm({...bookingForm, notes: e.target.value})} />
              
              <div className="flex gap-2 mt-4">
                <button type="submit" disabled={providerServices.length===0} style={{flex: 1}}>Confirm Booking</button>
                <button type="button" onClick={() => setSelectedProvider(null)} style={{flex: 1, backgroundColor: '#444', color: '#fff'}}>Cancel Modal</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserDashboard;
