import React, { Component } from 'react';

class Alerts extends Component {
    render() {
        return (
          <div className="row justify-content-end">
            <div className="alert alert-success alert-message col-4" id="success-alert">
                <span id="success-message-text"></span>
            </div>
            <div className="alert alert-danger alert-message col-4" id="error-alert" >
                <span id="error-message-text"></span>
            </div>
          </div>
        );
      }

}

export default Alerts;
