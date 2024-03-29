document.addEventListener('DOMContentLoaded', function () {
    let pathname = window.location.pathname;

    // Extract the file name from the pathname (e.g., "file.html")
    let fileName = pathname.substring(pathname.lastIndexOf('/') + 1);

    const recipeId = fileName.split('.')[0];
    const commentForm = document.getElementById('commentForm');
    const commentsSection = document.getElementById('commentsSection');

    const getCommentsUrl = 'https://flavorsmessages.azurewebsites.net/api/blobs/comments?recipeId=' + recipeId;
    const postCommentUrl = 'https://flavorsmessages.azurewebsites.net/api/blobs/post-comments';


    function fetchComments() {
        fetch(getCommentsUrl)
            .then(response => response.json())
            .then(data => {
                commentsSection.innerHTML = ''; // Clear existing comments
                data[0].comments.forEach(comment => {
                    appendComment(comment);
                });
            })
            .catch(error => console.error('Error fetching comments:', error));
    }

    function appendComment(comment) {
        const commentDiv = document.createElement('div');
        commentDiv.className = 'delicious-comments mt-30';
        commentDiv.innerHTML = `
            <p><strong>${comment.name} </strong></p>
            <p style="white-space: pre-line;">${comment.comment}</p>
            <p><small>${comment.timestamp}</small></p>
        `;
        commentsSection.appendChild(commentDiv);
    }

    // Add event listener to handle form submission
    commentForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const name = document.getElementById('name').value.trim();;
        const userEmail = document.getElementById('email').value.trim();;
        const commentText = document.getElementById('comment').value.trim();;

        // Check if name or commentText is empty
        if (!name || !commentText) {
            alert('Name and Comment should not be empty!');
            e.preventDefault(); // Prevent form submission
            return;
        }
        
        // Validate email format
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(userEmail)) {
            alert('Please enter a valid email address!');
            e.preventDefault(); // Prevent form submission
            return;
        }

        const newComment = [
            {
                recipeId: recipeId, 
                comments: [
                    {
                        userEmail: userEmail,
                        name: name,
                        comment: commentText,
                        timestamp: new Date().toISOString()
                    }
                ]
            }
        ];

        // Send POST request to add a new comment
        fetch(postCommentUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newComment)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                // Fetch and display all comments including the new one
                fetchComments();
                // Clear the form
                commentForm.reset();
            })
            .catch(error => console.error('Error posting comment:', error));
    });

    // Initial fetch to load and display existing comments
    fetchComments();
    });


