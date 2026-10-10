import { BrowserRouter, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import Browse from './pages/Browse';
import CreateListing from './pages/CreateListing';
import ItemDetails from './pages/ItemDetails';
import MyRentals from './pages/MyRentals';
import OwnerRequests from './pages/OwnerRequests';
import MyListings from './pages/MyListings';
import Favorites from './pages/Favorites';
import VerifyEmail from './pages/VerifyEmail';
import Profile from './pages/Profile';

function Nav() {
  const { token, userEmail, logout } = useAuth();
  return (
    <nav className="navbar">
      <Link to="/" className="brand">QuickLease</Link>
      <Link to="/">Browse</Link>
      {token ? (
        <>
          <Link to="/create">Create Listing</Link>
          <Link to="/my-rentals">My Rentals</Link>
          <Link to="/owner-requests">Owner Requests</Link>
          <Link to="/my-listings">My Listings</Link>
          <Link to="/favorites">Favorites</Link>
          <Link to="/profile">Profile</Link>
          <span className="user-email">{userEmail}</span>
          <button onClick={logout}>Logout</button>
        </>
      ) : (
        <>
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
        </>
      )}
    </nav>
  );
}

function ProtectedRoute({ children }) {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Nav />
        <Routes>
          <Route path="/" element={<Browse />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/create" element={<ProtectedRoute><CreateListing /></ProtectedRoute>} />
          <Route path="/items/:id" element={<ItemDetails />} />
          <Route path="/my-rentals" element={<ProtectedRoute><MyRentals /></ProtectedRoute>} />
          <Route path="/owner-requests" element={<ProtectedRoute><OwnerRequests /></ProtectedRoute>} />
          <Route path="/my-listings" element={<ProtectedRoute><MyListings /></ProtectedRoute>} />
          <Route path="/favorites" element={<ProtectedRoute><Favorites /></ProtectedRoute>} />
          <Route path="/verify-email" element={<VerifyEmail />} />
          <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;