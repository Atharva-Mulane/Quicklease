import { useState, useEffect } from 'react';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';

function MyListings() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState('');
  const { userEmail } = useAuth();

  const load = () => {
    api.get('/items')
      .then((res) => setItems(res.data.filter((i) => i.ownerName && userEmail)))
      .catch(() => setError('Failed to load'));
  };

  useEffect(() => { load(); }, []);

  const handleDelete = async (id) => {
    try {
      await api.delete(`/items/${id}`);
      load();
    } catch (err) {
      setError(err.response?.data || 'Delete failed');
    }
  };

  return (
    <div className="page">
      <h2>My Listings</h2>
      {error && <p className="msg-error">{error}</p>}
      {items.map((item) => (
        <div key={item.id} className="list-card">
          <h3>{item.title}</h3>
          <p className="price">₹{item.pricePerDay}/day — {item.category}</p>
          <button onClick={() => handleDelete(item.id)} className="danger">Delete</button>
        </div>
      ))}
    </div>
  );
}

export default MyListings;