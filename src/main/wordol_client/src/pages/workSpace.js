import React from 'react';
import {Layout} from 'antd';
import WorkSpaceHeader from '../component/workSpace/workSpaceHeader'
class WorkSpace extends React.Component{
    render(){
        return(
            <Layout className="layout">
                <Layout.Header>
                    <WorkSpaceHeader userName="U"></WorkSpaceHeader>
                </Layout.Header>
                <Layout.Content>

                </Layout.Content>
            </Layout>
        )
    }
}
export default WorkSpace;