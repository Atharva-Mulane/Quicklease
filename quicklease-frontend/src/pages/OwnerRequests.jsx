import { useState, useEffect } from 'react';
import api from '../api/axios';

function OwnerRequests() {
  const [rentals, setRentals] = useState([]);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const loadRentals = () => {
    api.get('/rentals/owner-requests')
      .then((res) => setRentals(res.data))
      .catch(() => setError('Failed to load requests'));
  };

  useEffect(() => {
    loadRentals();
  }, []);

  const handleAccept = async (id) => {
    setMessage('');
    try {
      await api.put(`/rentals/${id}/accept`);
      setMessage('Accepted!');
      loadRentals();
    } catch (err) {
      setMessage(err.response?.data || 'Action failed');
    }
  };

  const handleReject = async (id) => {
    setMessage('');
    try {
      await api.put(`/rentals/${id}/reject`);
      setMessage('Rejected.');
      loadRentals();
    } catch (err) {
      setMessage(err.response?.data || 'Action failed');
    }
  };

  return (
    <div className="page">
      <h2>Requests on My Items</h2>
      {error && <p className="msg-error">{error}</p>}
      {message && <p className="msg-success">{message}</p>}
      {rentals.length === 0 && <p>No requests yet.</p>}
      {rentals.map((r) => (
        <div key={r.id} className="list-card">
          <h3>{r.itemTitle}</h3>
          <p>Renter: {r.renterName}</p>
          <p>{r.startDate} → {r.endDate}</p>
          <p className="price">₹{r.totalPrice}</p>
          <span className={`badge ${r.status}`}>{r.status}</span>
          {r.status === 'PENDING' && (
            <div style={{ marginTop: '0.8rem' }}>
              <button onClick={() => handleAccept(r.id)}>Accept</button>
              <button onClick={() => handleReject(r.id)} className="secondary">Reject</button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default OwnerRequests;