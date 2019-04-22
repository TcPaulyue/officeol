import React from 'react';
import {Avatar} from 'antd';
class WorkSpaceHeader extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            userName:this.props.userName
        }
    }
    render(){
        return(
            <div style={{float:"right",verticalAlign:'middle'}}>
                <Avatar style={{backgroundColor:"#f56a00"}} size="large">
                    {this.state.userName}
                </Avatar>
            </div>
        )
    }
}

export default WorkSpaceHeader;