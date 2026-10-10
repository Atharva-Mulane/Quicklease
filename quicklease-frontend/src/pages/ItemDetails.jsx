import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';

function ItemDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();
  const [item, setItem] = useState(null);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [reviews, setReviews] = useState([]);
  const today = new Date().toLocaleDateString('en-CA');

  useEffect(() => {
    api.get(`/items/${id}`)
      .then((res) => setItem(res.data))
      .catch(() => setError('Item not found'));
    api.get(`/reviews/item/${id}`)
      .then((res) => setReviews(res.data))
      .catch(() => {});
  }, [id]);

  const handleFavorite = async () => {
    try {
      await api.post(`/items/${id}/favorite`);
      alert('Added to favorites!');
    } catch (err) {
      alert(err.response?.data || 'Failed');
    }
  };

  const handleRent = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    if (!token) {
      navigate('/login');
      return;
    }
    try {
      const res = await api.post('/rentals', {
        itemId: parseInt(id),
        startDate,
        endDate,
      });
      setSuccess(`Request sent! Total: ₹${res.data.totalPrice}. Status: ${res.data.status}`);
    } catch (err) {
      setError(err.response?.data || 'Rental request failed');
    }
  };

  if (error && !item) return <p style={{ padding: '2rem', color: 'red' }}>{error}</p>;
  if (!item) return <p style={{ padding: '2rem' }}>Loading...</p>;

  return (
    <div className="page">
      <div className="form-box" style={{ maxWidth: '600px' }}>
        <h2>{item.title}</h2>
        {item.imageUrl && <img src={item.imageUrl} alt={item.title} className="detail-img" />}
        <p>{item.description}</p>
        <p className="price">₹{item.pricePerDay}/day</p>
        <p>Category: {item.category}</p>
        <p>Security Deposit: ₹{item.securityDeposit || 0}</p>
        <p>Owner: {item.ownerName}</p>

        {token && <button className="fav-btn" onClick={handleFavorite}>♥ Add to Favorites</button>}

        <h3 style={{ marginTop: '1.5rem' }}>Rent this item</h3>
        <form onSubmit={handleRent}>
          <label style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>Start date</label>
          <input
            type="date"
            min={today}
            value={startDate}
            onChange={(e) => {
              setStartDate(e.target.value);
              setEndDate('');
            }}
            required
          />
          <label style={{ fontSize: '0.85rem', color: 'var(--text-dim)' }}>End date</label>
          <input
            type="date"
            min={startDate || today}
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            required
          />
          <button type="submit">Request to Rent</button>
        </form>
        {error && <p className="msg-error">{JSON.stringify(error)}</p>}
        {success && <p className="msg-success">{success}</p>}

        <h3 style={{ marginTop: '1.5rem' }}>Reviews</h3>
        {reviews.length === 0 && <p style={{ color: 'var(--text-dim)' }}>No reviews yet.</p>}
        {reviews.map((r) => (
          <div key={r.id} style={{ borderTop: '1px solid var(--border)', padding: '0.6rem 0' }}>
            <p>{'⭐'.repeat(r.rating)} — {r.reviewerName}</p>
            <p style={{ color: 'var(--text-dim)' }}>{r.comment}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ItemDetails;