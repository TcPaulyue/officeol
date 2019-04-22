import React from 'react';
import WebSocketForOLEditor from './WebSocketForOLEditor';
import Quill from 'quill';
import 'quill/dist/quill.snow.css';
import MessageHandler from './messageHandler/MessageHandler';
class QuillEditor extends React.Component{
  constructor(props){
    super(props);
    this.isComposition = 'end';

    

    //editor and delta
    this.editor = null;
    this.selection = null;

    this.compositionBuffer = [];

    //binding
    this.handleTextChange = this.handleTextChange.bind(this);
    this.handleMessage = this.handleMessage.bind(this);
    this.handleSelectionChange = this.handleSelectionChange.bind(this);
    this.state = {
      webSocket:WebSocketForOLEditor.createSocket(this.props.userId,this.props.fileId,this.handleMessage)
    }
    //this.webSocket = WebSocketForOLEditor.createSocket(this.props.userId,this.props.fileId,this.handleMessage);
    this.messageHandler = new MessageHandler(this.state.webSocket,this.props.userId,this.props.fileId);
    this.handleComposition = this.handleComposition.bind(this);
    this.handleCompositionBuffer = this.handleCompositionBuffer.bind(this);
  }
  componentDidMount(){
    const options = {
      debug: "warn",
      theme: "snow"
    };
    if(this.editor==null) {
      this.editor = new Quill("#editor", options);
    }
    this.nullDelta = this.editor.getContents();
    this.editor.on('text-change',this.handleTextChange);
    this.editor.on('selection-change',this.handleSelectionChange);
  }
  

  handleSelectionChange(range,oldRange,source){
    this.selection = range;
    console.log(range);
  }
  handleTextChange(delta,oldDelta,source){
    if(source==='user') {
      
      //没有使用输入法输入
      if(this.isComposition === 'end'){
        this.messageHandler.generate(delta['ops']);
      }
      //正在使用输入法输入,拦截所有delta变化
      else{
        let oldOps = delta['ops'];
        //let newOps = [];
        if(this.isComposition==='start'){
          this.isComposition = 'composing';
          // oldOps.forEach((value)=>{
          //   if(!value.hasOwnProperty('insert')){
          //     newOps.push(value);
          //   }
          // })
          // this.messageHandler.generate(newOps);
          this.compositionBuffer.push(oldOps);
        }
        else{
          this.compositionBuffer.push(oldOps);
        }
      }
    }
  }
  handleMessage(evt){
    let message = JSON.parse(evt.data);
    console.log(message);
    if(message['userId']!==this.props.userId){
      if(this.isComposition ==='end'){
        let deltaOps = this.messageHandler.receive(message);
        if(deltaOps!==null){
          this.editor.updateContents(deltaOps);
        }
      }
      else{
        this.messageHandler.receiveToBuffer(message);
      }
    }
  }


  handleCompositionBuffer(){
    let delta = this.editor.getContents();
    delta['ops'] = [];
    this.compositionBuffer.forEach((deltaOps)=>{
      delta = delta.compose({ops:deltaOps});
    })
    this.compositionBuffer = [];
    console.log(delta['ops']);
    return delta['ops'];
  }

  handleComposition(e){
    if(e.type==='compositionstart'){
      this.isComposition = 'start';
      console.log('start');
    }
    else if(e.type==='compositionupdate'){
      console.log(e.data);
    }
    else{
      console.log('end');
      this.messageHandler.generate(this.handleCompositionBuffer());
      this.editor.disable();
      let deltaOpsList = this.messageHandler.applyBuffer();
      deltaOpsList.forEach((deltaOps)=>{
        this.editor.updateContents(deltaOps);
      })
      this.editor.enable();
      this.editor.focus();
      
      this.isComposition = 'end';
    }
  }


  render(){
    
    return(
      <div id="editor"
        onCompositionStart={this.handleComposition}
        onCompositionEnd={this.handleComposition}
        onCompositionUpdate={this.handleComposition}
      />
    )
  }

}


export default QuillEditor;
