import { useState, useEffect } from 'react';
import api from '../api/axios';

function Favorites() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState('');

  const load = () => {
    api.get('/items/favorites')
      .then((res) => setItems(res.data))
      .catch(() => setError('Failed to load favorites'));
  };

  useEffect(() => { load(); }, []);

  const handleRemove = async (id) => {
    try {
      await api.delete(`/items/${id}/favorite`);
      load();
    } catch (err) {
      setError(err.response?.data || 'Failed to remove');
    }
  };

  return (
    <div className="page">
      <h2>My Favorites</h2>
      {error && <p className="msg-error">{error}</p>}
      {items.length === 0 && <p>No favorites yet.</p>}
      <div className="grid">
        {items.map((item) => (
          <div key={item.id} className="card">
            <h3>{item.title}</h3>
            <p className="price">₹{item.pricePerDay}/day</p>
            <button onClick={() => handleRemove(item.id)} className="danger" style={{ marginTop: '0.5rem' }}>
              Remove
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Favorites;