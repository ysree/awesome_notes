/// ==UserScript==
// @name         Fill Username
// @namespace    https://gitlab.eng.vmware.com/alalev/tampermonkey-scripts/
// @version      1.0.3
// @description  Automatically enters administrator@vsphere.local
// @include      *://*.eng.vmware.com/websso/SAML2/SSO/*
// @include      *://*/websso/SAML2/SSO/*
// @include      *10.126.24.52/vsphere-client/?cs
// @require      http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js
// @grant        none
// ==/UserScript==
const styleElementHtml = `<style>
#loginForm {
   transition: width 500ms;
   width: 24rem;
}
#new-ui-login-button-row button {
   max-width: unset;
   width: 100%;
}
</style>`;
(function () {
   /** @type {HTMLElement | HTMLDivElement} */
   let loginButtonRow;
   /** @type {HTMLInputElement} */
   let usernameInput;
   /** @type {HTMLInputElement} */
   let passwordInput;
   /** @type {HTMLInputElement} */
   let loginButton;
   function tryUser(user) {
      if ($('#progressBar').is(':visible')) {
         return;
      }
      $(usernameInput).val(user.username);
      $(passwordInput).val(user.password);
      $(loginButton).prop('disabled', false).click();
   }
   $(() => {
      $(document.head).append(styleElementHtml);
      const users = [{
         username: 'Administrator@vsphere.local',
         password: 'Admin!23'
      }, {
         username: 'Administrator@wcp.local',
         password: 'Admin!23'
      }, {
         username: 'cloudadmin@vmc.local',
         password: 'VMware123!'
      }, {
         username: 'Administrator@skyscraper.local',
         password: 'Admin!23'
      }, {
         username: 'cloudAdminUser@skyscraper.local',
         password: 'VMware@123'
      }];
      /** @type {boolean} */
      let isOldUi = true;
      loginButtonRow = $('#loginButtonRow')[0];
      if (loginButtonRow) {
         // Old UI
         usernameInput = $('input#username')[0];
         passwordInput = $('input#password')[0];
         loginButton = $('input#submit')[0];
      } else {
         // New UI
         isOldUi = false;
         loginButtonRow = document.createElement('div');
         loginButtonRow.id = 'new-ui-login-button-row';
         /** @type {HTMLElement} */
         const loginForm = $('#loginForm')[0];
         loginForm.appendChild(loginButtonRow);
         usernameInput = $('#loginForm input#username')[0];
         passwordInput = $('#loginForm input#password')[0];
         loginButton = $('#loginForm input#submit')[0];
      }
      /** @type {string} */
      const buttonClass = isOldUi ? 'blue' : 'btn';
      /** @type {string} */
      const goodColour = isOldUi ? 'cyan' : 'blue';
      /** @type {string} */
      const warningColour = 'orange';
      if (loginButtonRow) {
         for (let i = 0; i < users.length; i++) {
            const currentUser = users[i];
            const button = document.createElement('button');
            $(button)
                  .html(`Log in as <span style="color: ${goodColour};">${users[i].username}</span>`)
                  .addClass('button').addClass(buttonClass)
                  .click(function () {
                     tryUser(currentUser);
                     return false;
                  });
            loginButtonRow.appendChild(button);
         }
      } else {
         console.warn('The login button row has not been found.');
      }
      if (window.location.href.indexOf('sof-rila') !== -1) {
         return;
      }
      /** @type {HTMLButtonElement} */
      const button = document.createElement('button');
      button.id = 'do-not-do-default-login-button';
      let automaticLoginSecondsLeft = 3;
      let automaticLoginInterval;
      automaticLoginInterval = setInterval(() => {
         const doNotLoginButton = $('#do-not-do-default-login-button')[0];
         if (!doNotLoginButton) {
            return;
         }
         if (!!$(usernameInput).val() || !!$(passwordInput).val()) {
            doNotLoginButton.parentElement.removeChild(doNotLoginButton);
            clearInterval(automaticLoginInterval);
         } else if (automaticLoginSecondsLeft === 0) {
            doNotLoginButton.parentElement.removeChild(doNotLoginButton);
            tryUser(users[0]);
            clearInterval(automaticLoginInterval);
         } else {
            doNotLoginButton.innerHTML = doNotLoginButton.innerHTML.replace(automaticLoginSecondsLeft + '...', (automaticLoginSecondsLeft - 1) + '...');
         }
         automaticLoginSecondsLeft--;
      }, 1000);
      $(button)
            .html(`Do <span style="color: ${warningColour};">NOT</span> log in as
<span style="color: ${goodColour};">${users[0].username}</span> <span style="color: ${warningColour};">(${automaticLoginSecondsLeft}...)</span>`)
            .addClass('button').addClass(buttonClass)
            .click(function () {
               button.parentElement.removeChild(button);
               clearInterval(automaticLoginInterval);
               return true;
            });
      loginButtonRow.appendChild(button);
   });
})();
