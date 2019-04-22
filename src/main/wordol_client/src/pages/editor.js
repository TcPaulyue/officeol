import React from 'react';
import QuillEditor from '../component/QuillEditor';
class Editor extends React.Component{
      render(){
        let userId = this.props.location.query.userId;
        let fileId = this.props.location.query.fileId;
        return(
          <QuillEditor userId={userId} fileId={fileId}/>
        )
      }
}

export default Editor;
