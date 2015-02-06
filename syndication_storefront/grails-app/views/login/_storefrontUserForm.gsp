<form action="updateUserAccount" id='loginForm'>
    <div class="register_box">
        <div>
            <div class="left_side">
                <p>Full Name <span class="red">*</span></p>
            </div>
            <label for="name" class="obscure">Full Name</label>
            <div class="right_side_name ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                <g:textField id="name" name="name" value="${userInstance?.name}" autocomplete="off"/>
            </div>
            <div class="below_text">
                <p class="italic">Enter your name (First Name, Middle Initial, Last Name).</p>
            </div>
        </div>
        <div>
            <div class="left_side">
                <p>E-mail<span class="red"></span></p>
            </div>

            <div class="right_side ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
                <span>${userInstance?.username}</span>
            </div>
        </div>
        <br />
        <p>Password must:</p>
        <ul class="register_set">
            <li>Be at least 8 characters</li>
            <li>Contain at least one uppercase letter</li>
            <li>Contain at least one lowercase letter</li>
            <li>Contain at least one number</li>
        </ul>


        <div>
            <div class="left_side">
                <p>New Password <span class="red">*</span></p>
            </div>

            <div class="right_side ${hasErrors(bean: userInstance, field: 'password', 'errors')}">
                <g:passwordField name="password"/>
            </div>
        </div>

        <div>
            <div class="left_side">
                <p>Re-enter New Password<span class="red">*</span></p>
            </div>

            <div class="right_side ${hasErrors(bean: userInstance, field: 'passwordRepeat', 'errors')}">
                <g:passwordField name="passwordRepeat"/>
            </div>
        </div>
        <br/>
        <div class="input_row">
            <g:submitButton name="updateChangesButton" value="Update"/>
            <g:actionSubmit name="cancelButton" action="index" value="Cancel"/>
        </div>
    </div>
</form>