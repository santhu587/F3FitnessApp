const express = require('express');
const cors = require('cors');
const nodemailer = require('nodemailer');
const dotenv = require('dotenv');

// Load environment variables
dotenv.config();

const app = express();

// Middleware
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static('public')); // Serve static files from 'public' directory

// Create a transporter for sending emails
const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.EMAIL_USER,
        pass: process.env.EMAIL_PASS
    }
});

// Route to handle gym registration form submissions
app.post('/api/register', async (req, res) => {
    try {
        const {
            fullName,
            email,
            phone,
            age,
            gender,
            membershipType,
            goals,
            medicalConditions,
            preferredTime,
            additionalInfo
        } = req.body;

        // Email options
        const mailOptions = {
            from: process.env.EMAIL_USER,
            to: 'santhoshckm2001@gmail.com',
            subject: 'New Gym Registration',
            html: `
                <h2>New Gym Registration</h2>
                <p><strong>Full Name:</strong> ${fullName}</p>
                <p><strong>Email:</strong> ${email}</p>
                <p><strong>Phone:</strong> ${phone}</p>
                <p><strong>Age:</strong> ${age}</p>
                <p><strong>Gender:</strong> ${gender}</p>
                <p><strong>Membership Type:</strong> ${membershipType}</p>
                <p><strong>Fitness Goals:</strong> ${goals}</p>
                <p><strong>Medical Conditions:</strong> ${medicalConditions || 'None'}</p>
                <p><strong>Preferred Time:</strong> ${preferredTime}</p>
                <p><strong>Additional Information:</strong> ${additionalInfo || 'None'}</p>
            `
        };

        // Send email
        await transporter.sendMail(mailOptions);

        res.status(200).json({ 
            success: true,
            message: 'Registration submitted successfully! We will contact you soon.' 
        });
    } catch (error) {
        console.error('Error sending registration email:', error);
        res.status(500).json({ 
            success: false,
            error: 'Failed to submit registration. Please try again later.' 
        });
    }
});

// Basic route for testing
app.get('/', (req, res) => {
    res.send('Server is running!');
});

// Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
}); 