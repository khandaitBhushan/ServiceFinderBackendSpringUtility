import React, { useState, useEffect } from 'react';
import { api } from '../utils/api';
import { CheckCircle, XCircle } from 'lucide-react';

const ProviderDashboard = () => {
  const [services, setServices] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [newService, setNewService] = useState({ serviceName: '', description: '', price: 100, durationMinutes: 60, isActive: true });

  const loadData = () => {
    api.providerServices.getMy().then(setServices).catch(console.log);
    api.bookings.getProviderBookings().then(setBookings).catch(console.log);
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddService = async (e) => {
    e.preventDefault();
    try {
      await api.providerServices.add({
        ...newService,
        price: parseFloat(newService.price),
        durationMinutes: parseInt(newService.durationMinutes)
      });
      setNewService({ serviceName: '', description: '', price: 100, durationMinutes: 60, isActive: true });
      loadData();
    } catch (err) {
      alert("Failed to add service: " + err.message);
    }
  };

  const updateBooking = async (id, status) => {
    try {
      await api.bookings.updateStatus(id, status);
      loadData();
    } catch (err) {
      alert("Failed to update status");
    }
  };

  const cancelBooking = async (id) => {
    try {
      if(!window.confirm("Are you sure you want to completely cancel this booking?")) return;
      await api.bookings.cancel(id);
      loadData();
    } catch(err) {
      alert("Failed to cancel booking: " + err.message);
    }
  };

  return (
    <div className="w-full">
      <h2>Provider Dashboard</h2>
      
      <div className="grid md-grid-cols-2 gap-4">
        <div>
          <div className="card">
            <h3>Add New Service Slot</h3>
            <form onSubmit={handleAddService} className="flex-col">
              <input type="text" placeholder="Service Name (e.g. Switch Installation)" required
                value={newService.serviceName} onChange={e => setNewService({...newService, serviceName: e.target.value})} />
              <input type="text" placeholder="Description" required
                value={newService.description} onChange={e => setNewService({...newService, description: e.target.value})} />
              <div className="grid grid-cols-2 gap-2">
                <input type="number" placeholder="Price (₹)" required
                  value={newService.price} onChange={e => setNewService({...newService, price: e.target.value})} />
                <input type="number" placeholder="Duration (mins)" required
                  value={newService.durationMinutes} onChange={e => setNewService({...newService, durationMinutes: e.target.value})} />
              </div>
              <button type="submit">Add Service</button>
            </form>
          </div>

          <div className="card">
            <h3>My Active Services</h3>
            {services.length === 0 ? <p className="text-gray">No services customized yet.</p> : null}
            {services.map(s => (
              <div key={s.id} className="p-4 mb-4" style={{border: '1px solid #333', borderRadius: '8px'}}>
                <h4 style={{margin: '0 0 5px 0'}}>{s.serviceName}</h4>
                <p className="text-sm text-gray" style={{margin: '0 0 10px 0'}}>{s.description}</p>
                <div className="flex justify-between text-sm">
                  <span className="text-blue">₹{s.price}</span>
                  <span>{s.durationMinutes} mins</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div>
          <div className="card">
            <h3>Incoming Bookings</h3>
            {bookings.length === 0 ? <p className="text-gray">No bookings received.</p> : null}
            {bookings.map(b => (
              <div key={b.id} className="p-4 mb-4" style={{border: '1px solid #333', borderRadius: '8px'}}>
                <h4 style={{margin: '0 0 5px 0'}}>
                  {b.service?.serviceName || 'General Consultation'} 
                  <span style={{float: 'right', fontSize: '12px'}} className={b.status === 'PENDING' ? 'text-blue' : b.status === 'CONFIRMED' ? 'text-green' : 'text-red'}>
                    {b.status}
                  </span>
                </h4>
                <p className="text-sm">Client: {b.user?.name} ({b.user?.phone})</p>
                <p className="text-sm">Address: {b.serviceAddress || 'N/A'}</p>
                <p className="text-sm">Time: {new Date(b.date).toLocaleString()}</p>
                {b.userNotes && <p className="text-sm text-gray">Notes: "{b.userNotes}"</p>}
                
                {b.status === 'PENDING' && (
                  <div className="flex gap-2 mt-4">
                    <button className="flex items-center justify-center gap-2" style={{backgroundColor: '#2ecc71', color: '#fff', flex: 1}} onClick={() => updateBooking(b.id, 'CONFIRMED')}>
                      <CheckCircle size={16}/> Confirm
                    </button>
                    <button className="flex items-center justify-center gap-2" style={{backgroundColor: '#e74c3c', color: '#fff', flex: 1}} onClick={() => updateBooking(b.id, 'REJECTED')}>
                      <XCircle size={16}/> Reject
                    </button>
                  </div>
                )}
                {['PENDING', 'CONFIRMED'].includes(b.status) && (
                   <button className="w-full mt-2 text-sm" style={{backgroundColor: '#333', color: '#e74c3c'}} onClick={() => cancelBooking(b.id)}>
                      Force Cancel Booking
                   </button>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProviderDashboard;
