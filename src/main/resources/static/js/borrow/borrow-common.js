// Shared module for borrow functionality - provides common utilities for both borrow.js and return.js

// Enhanced message display function - shared across borrow pages
function showMessage(message, type = "info") {
    // Remove existing messages
    const existingMessages = document.querySelectorAll('.message');
    existingMessages.forEach(msg => msg.remove());

    // Create new message element
    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.style.cssText = `
        padding: 10px;
        margin: 10px 0;
        border-radius: 4px;
        font-weight: bold;
        ${type === 'error' ? 'background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;' : ''}
        ${type === 'success' ? 'background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : ''}
        ${type === 'info' ? 'background-color: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb;' : ''}
    `;
    messageDiv.textContent = message;

    // Insert message at the top of the main content
    const main = document.querySelector('main');
    if (main) {
        main.insertBefore(messageDiv, main.firstChild);
    } else {
        // Fallback if main element not found
        document.body.insertBefore(messageDiv, document.body.firstChild);
    }

    // Auto-remove after 5 seconds
    setTimeout(() => {
        if (messageDiv.parentNode) {
            messageDiv.remove();
        }
    }, 5000);
}

// Global variable to store all borrow records - shared across modules
let allBorrowRecords = [];

// Shared function to load active borrow records - used by both pages
async function loadActiveBorrowsShared() {
    try {
        const response = await fetch("/api/borrow/records");

        if (!response.ok) {
            console.error("API Error:", response.status, response.statusText);
            const errorText = await response.text();
            console.error("Error details:", errorText);
            showMessage("Failed to load borrow records. Please check the application logs.", "error");
            return;
        }

        const borrowRecords = await response.json();

        // Ensure borrowRecords is an array
        if (!Array.isArray(borrowRecords)) {
            console.error("Expected array but got:", typeof borrowRecords, borrowRecords);
            showMessage("Invalid response format from server.", "error");
            return;
        }

        // Store all records globally for filtering
        allBorrowRecords = borrowRecords;

        // Return records for use by specific page implementations
        return borrowRecords;
    } catch (error) {
        console.error("Failed to load active borrow records:", error);
        showMessage("An error occurred while loading borrow records: " + error.message, "error");
        return [];
    }
}

// Shared function to select borrow record
function selectBorrowRecordShared(borrowId) {
    document.getElementById("borrowRecordSelect").value = borrowId;
    document.getElementById("borrowRecordId").value = borrowId;
    
    // Scroll to form
    const borrowContainer = document.querySelector(".borrow-container");
    if (borrowContainer) {
        borrowContainer.scrollIntoView({ behavior: "smooth" });
    }
}

// Shared renew function: shows modal, posts renew request, updates records and notifies pages
async function renewBorrowRecord(borrowId) {
    const record = allBorrowRecords.find(r => r.borrowID === borrowId);
    if (!record) {
        showMessage('Borrow record not found!', 'error');
        return;
    }

    const currentDueDate = new Date(record.dueDate);
    const suggestedDate = new Date(currentDueDate);
    suggestedDate.setDate(suggestedDate.getDate() + 7);

    const formatDate = (d) => d.toISOString().split('T')[0];

    const modalOverlay = document.createElement('div');
    modalOverlay.id = 'renewModal';
    modalOverlay.style.cssText = `position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; z-index: 1000;`;

    const modalContent = document.createElement('div');
    modalContent.className = 'modal-content';
    modalContent.style.cssText = 'max-width:400px;padding:20px;background:white;border-radius:8px;box-shadow:0 4px 20px rgba(0,0,0,0.3);';

    modalContent.innerHTML = `
        <h3>Renew Borrow ID ${borrowId}</h3>
        <p>Current due: ${formatDate(currentDueDate)}</p>
        <label>New due: <input type="date" id="renewDate" min="${formatDate(currentDueDate)}" value="${formatDate(suggestedDate)}"></label>
        <div class="modal-buttons" style="margin-top:12px;text-align:right;">
            <button id="cancelRenew" type="button" class="borrow-button">Cancel</button>
            <button id="confirmRenew" type="button" class="borrow-button">Renew</button>
        </div>
    `;

    modalOverlay.appendChild(modalContent);
    document.body.appendChild(modalOverlay);

    modalOverlay.addEventListener('click', e => { if (e.target === modalOverlay) modalOverlay.remove(); });
    document.getElementById('cancelRenew').onclick = () => modalOverlay.remove();

    document.getElementById('confirmRenew').onclick = async () => {
        const newDateStr = document.getElementById('renewDate').value;
        const newDate = new Date(newDateStr);

        if (!newDateStr || newDate <= currentDueDate) {
            showMessage('Date must be after current due date', 'error');
            return;
        }

        const days = Math.ceil((newDate - currentDueDate) / (1000 * 60 * 60 * 24));

        try {
            const res = await fetch(`/api/borrow/renew/${borrowId}?days=${days}`, { method: 'POST' });
            if (res.ok) {
                showMessage('Renewed!', 'success');
                // Refresh shared records and notify pages to re-render
                await loadActiveBorrowsShared();
                document.dispatchEvent(new CustomEvent('borrowRecordsUpdated'));
            } else {
                const text = await res.text();
                showMessage(text || 'Failed to renew', 'error');
            }
        } catch (err) {
            showMessage('Network error: ' + err.message, 'error');
        } finally {
            modalOverlay.remove();
        }
    };
}

// Export functions for use in other modules (if using modules)
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        showMessage,
        loadActiveBorrowsShared,
        selectBorrowRecordShared,
        allBorrowRecords
    };
}
