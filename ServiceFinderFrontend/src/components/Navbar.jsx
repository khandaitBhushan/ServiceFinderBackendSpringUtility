import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { LogOut, Navigation } from 'lucide-react';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="flex items-center gap-2">
        <Navigation color="#1E90FF" />
        <h2 className="text-blue">Service Utility Finder</h2>
      </div>
      <div className="navbar-links flex items-center">
        {user ? (
          <>
            <span style={{ marginRight: '15px' }}>Hello, {user.name}</span>
            <button className="flex items-center gap-2" onClick={handleLogout}>
              <LogOut size={16} /> Logout
            </button>
          </>
        ) : (
          <>
            <button onClick={() => navigate('/login')}>Login</button>
            <button onClick={() => navigate('/register')}>Register</button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
