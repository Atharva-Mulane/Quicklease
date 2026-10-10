import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/axios';

function Browse() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/items')
      .then((res) => setItems(res.data))
      .catch(() => setError('Failed to load items'));
  }, []);

  return (
    <div className="page">
      <h2>Browse Items</h2>
      <p className="subtitle">Find tools, gear, and equipment to rent near you</p>
      {error && <p className="msg-error">{error}</p>}
      <div className="grid">
        {items.map((item) => (
          <Link key={item.id} to={`/items/${item.id}`}>
            <div className="card">
            {item.imageUrl ? (
                <img src={item.imageUrl} alt={item.title} className="card-img" />
            ) : (
            <div className="card-img-placeholder">No image</div>
            )}
  <h3>{item.title}</h3>
              <p>{item.description}</p>
              <p className="price">₹{item.pricePerDay}/day</p>
              <p>{item.category} • {item.ownerName}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default Browse;