import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../utils/api';

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check token on mount
    const token = localStorage.getItem('token');
    const storedRole = localStorage.getItem('role');
    if (token) {
      setRole(storedRole);
      // Fetch fresh user data
      api.user.me().then(data => {
        setUser(data);
        setRole(data.role); // In case role wasn't stored properly
      }).catch((err) => {
        console.error("Token invalid, please login again", err);
        logout();
      }).finally(() => {
        setLoading(false);
      });
    } else {
      setLoading(false);
    }
  }, []);

  const login = async (email, password) => {
    const res = await api.auth.login({ email, password });
    if (res.success && res.token) {
      localStorage.setItem('token', res.token);
      localStorage.setItem('role', res.role);
      setRole(res.role);
      
      const me = await api.user.me();
      setUser(me);
      return me;
    }
    throw new Error(res.message);
  };

  const registerAndLogin = async (userData) => {
    const res = await api.auth.registerUser(userData);
    if (res.success && res.token) {
      localStorage.setItem('token', res.token);
      localStorage.setItem('role', res.role);
      setRole(res.role);
      
      const me = await api.user.me();
      setUser(me);
      return me;
    }
    throw new Error(res.message);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    setUser(null);
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ user, role, login, registerAndLogin, logout, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
