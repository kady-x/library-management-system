const pathParts = window.location.pathname.split('/');
const memberId = pathParts[pathParts.length - 1];

function validateMemberForm(form) {
    const name = form.name.value.trim();
    const contactInfo = form.contactInfo.value.trim();
    const membershipDate = form.membershipDate.value;

    if (!name) {
        showError("Name is required");
        return false;
    }

    if (name.length < 2) {
        showError("Name must be at least 2 characters long");
        return false;
    }

    if (!contactInfo) {
        showError("Contact information is required");
        return false;
    }

    if (!membershipDate) {
        showError("Membership date is required");
        return false;
    }

    const selectedDate = new Date(membershipDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (selectedDate > today) {
        showError("Membership date cannot be in the future");
        return false;
    }

    return true;
}

function showError(message) {
    const existingError = document.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }

    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.cssText = `
        background: #ff4757;
        color: white;
        padding: 12px;
        border-radius: 6px;
        margin-bottom: 20px;
        text-align: center;
        font-weight: bold;
    `;
    errorDiv.textContent = message;

    const form = document.getElementById("editMemberForm");
    form.parentNode.insertBefore(errorDiv, form);

    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, 5000);
}

function showSuccess(message) {
    const existingMessage = document.querySelector('.success-message, .error-message');
    if (existingMessage) {
        existingMessage.remove();
    }

    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.style.cssText = `
        background: #2ed573;
        color: white;
        padding: 12px;
        border-radius: 6px;
        margin-bottom: 20px;
        text-align: center;
        font-weight: bold;
    `;
    successDiv.textContent = message;

    const container = document.querySelector('.container');
    container.insertBefore(successDiv, container.firstChild);

    setTimeout(() => {
        if (successDiv.parentNode) {
            successDiv.remove();
        }
    }, 3000);
}

function setLoadingState(loading) {
    const submitBtn = document.querySelector('.add-btn-member');
    const resetBtn = document.querySelector('.reset-btn-member');
    const inputs = document.querySelectorAll('input');

    if (loading) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Updating Member...';
        resetBtn.disabled = true;
        inputs.forEach(input => input.disabled = true);
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Update Member';
        resetBtn.disabled = false;
        inputs.forEach(input => input.disabled = false);
    }
}

async function loadMember(id) {
    try {
        setLoadingState(true);

        const response = await fetch(`/api/members/${id}`);

        if (!response.ok) {
            showError("Member not found or failed to load");
            setTimeout(() => {
                window.location.href = "/members";
            }, 2000);
            return;
        }

        const member = await response.json();

        let membershipDate = member.membershipDate;
        if (membershipDate && membershipDate !== "0001-01-01") {
            const date = new Date(membershipDate);
            membershipDate = date.toISOString().split('T')[0];
        } else {
            membershipDate = new Date().toISOString().split('T')[0];
        }

        document.getElementById("memberID").value = member.memberId;
        document.querySelector("input[name='name']").value = member.name;
        document.querySelector("input[name='contactInfo']").value = member.contactInfo;
        document.querySelector("input[name='membershipDate']").value = membershipDate;

        showSuccess("Member data loaded successfully");

    } catch (error) {
        console.error("Failed to load member:", error);
        showError("Failed to load member data");
    } finally {
        setLoadingState(false);
    }
}

if (memberId) {
    loadMember(memberId);
} else {
    showError("Invalid member ID");
    setTimeout(() => {
        window.location.href = "/members";
    }, 2000);
}

document.getElementById("editMemberForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const form = e.target;
    const id = document.getElementById("memberID").value;

    if (!validateMemberForm(form)) {
        return;
    }

    const currentData = {
        name: form.name.value.trim(),
        contactInfo: form.contactInfo.value.trim(),
        membershipDate: form.membershipDate.value
    };

    setLoadingState(true);

    try {
        const response = await fetch(`/api/members/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(currentData)
        });

        if (response.ok) {
            showSuccess("Member updated successfully!");

            setTimeout(() => {
                window.location.href = "/members";
            }, 1500);
        } else {
            const error = await response.text();
            showError("Failed to update member: " + error);
        }
    } catch (error) {
        console.error("Network error:", error);
        showError("Network error occurred. Please try again.");
    } finally {
        setLoadingState(false);
    }
});
