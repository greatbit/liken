import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { withRouter } from 'react-router';

class Header extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
          <nav className="navbar navbar-expand-md navbar-dark bg-green">
            <Link className="navbar-brand" to="/">
                <img src='/images/smalllogoquack.png'/>
            </Link>
            <button type="button" className="navbar-toggler" data-toggle="collapse" data-target="#navbarNav">
              <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav mr-auto">
                    <li className="nav-item"><Link className="nav-link" to={"/launch"}>Launches</Link></li>
                </ul>
            </div>
          </nav>
        );
      }

}

export default withRouter(Header);
