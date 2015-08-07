// global note template 
function noteTemplate ( note_id, headline , description, color ){
	
		var desc = '',
			tpl =  [
					'<div>',
						'<div id="note_line_'+ note_id +'" class="drop_notes note-np {{color}}" data-color="{{color}}">',
							'<h6>',
								'<span id="note_headline_'+  note_id +'"  class="note-headline ed-parent">'+  headline +'</span>',
								'<span class="note-actions hide">',
									'<a href="#" id="edit_note_link_'+ note_id +'" class="edit_note">Edit</a>&nbsp;',
									'<a href="#" id="delete_note_link_'+ note_id +'" class="delete_note">[x]</a>',
								'</span>',	
							'</h6>',
							'{{description}}',
						'</div>',
					'</div>'
				].join('');
			
		if ( description ){
			description = linkify(description);                   
			description = description.replace(/\n/g, "<br />");
                        desc = ['<div class="note-desc ed-parent border-yes">',
                                    '<p id="note_description_'+ note_id +'">', 
                                        description,
                                    '</p>',
                               '</div>'].join('');
		} else {
                        desc = '<div class="note-desc ed-parent"></div>';
                }
                
		tpl = tpl.replace(/{{color}}/g, color);
		return tpl.replace(/{{description}}/g, desc );		
};
	
// ajax call methods to synchronize with server
var cansynch = {};

cansynch.version = 1 ;

cansynch.url  = '';

// prevent not to call another ajax update until one ajax is finished 
// problem is if server is not responding ?
cansynch.ajax_in_progress = false ;
cansynch.is_pause    = false ;

	
cansynch.statusmap 		= { 
	'new'    : 'create' , 
	'delete' : 'remove' , 
	'update' : 'update'  
};

cansynch.listen = function(){
		
        if( cansynch.is_pause ) return;						

        if( ! cansynch.ajax_in_progress ) {

                cansynch.ajax_in_progress  = true ;
                
                $.get(  
                    cansynch.url + cansynch.version +'?t='+ ( +new Date() ), 
                    {} , 
                    cansynch.callback , 
                    "json"
                );

        }
};


cansynch._insert = function( data ){

		var tpl 	= noteTemplate( data.note_id  , data.headline , data.description, data.color ) ,
			
			notes 	= $('#block_'+ data.question_number).find('.drop_notes') , 
			
			html 	= $( tpl ). find('.drop_notes').css('display', 'none'),
                        
                        index   = data.note_position  ;
           
                //if any node duplicate , remove it before insert
                $('#note_line_'+ data.note_id ).remove();
                
                if ( $.isNumeric( index ) && notes.eq( index ).length  ){

                        $( html ).insertBefore(  notes.eq( index  ) );
				
				
		} else { // if empty append
		
                        $("#block_"+ data.question_number )
                                .append( html );
		}
		
		$('#note_line_'+ data.note_id ).show('slow');
};

//If create new note, call insert function
cansynch.create = function( data ){ 			

		cansynch._insert( data );			
};

//remove note
cansynch.remove = function( data ){ 

		$('#note_line_'+ data.note_id ).hide('slow' , function(){
				$(this).remove();
		});
};

cansynch.update_datas  = [] ;

//If update note, remove the current note and insert a new one.
cansynch.update = function( data ){

		cansynch.update_datas.push( data ) ;
				
};

cansynch.do_update = function(){	
		
		var tmp = [] , tmpObj = {} , pos , i ; 
		
		for ( i = 0 ; i < cansynch.update_datas.length ; i++ ) {
		
				pos =  cansynch.update_datas[i].note_position ;
				tmp.push( pos );
				tmpObj[pos] = cansynch.update_datas[i];					
		}
		
		tmp.sort();	

		// update from queue
		for ( i = 0 ; i < tmp.length ; i++ ){
		
				data  = tmpObj[ tmp[i] ];				
				
				$('#note_line_'+data.note_id ).remove();
				cansynch._insert( data );
		
		}
		
};

cansynch.callback = function (data){
                   
		cansynch.ajax_in_progress = false;
		
		if( cansynch.is_pause ) return;
		
		var latest_revision =  parseInt( data.current_revision ,10 ) ;
		                   
		if (latest_revision == cansynch.version) {	
		
				return;
		} 
		
		cansynch.version  = latest_revision;	
		
		cansynch.update_datas = [];
						
		// loop through json data 
		// and call corresponding function
		for( var i = 0; i < data.detail.length  ; i++ ){
					
				var func = cansynch.statusmap[ data.detail[ i ].status ] ;								
				cansynch[ func ]( data.detail[ i ] );
		}	
		
		cansynch.do_update();		
};	

//Canvanizer interval is now 500ms (0.5 second) 
cansynch.init = function( params ){
		
		cansynch.version  	= parseInt( params.version , 10 );
		cansynch.url  		= params.url ;
		
		window.setInterval( cansynch.listen , can_update_interval );
		
};

cansynch.pause = function( status ){
		
		cansynch.is_pause = status ;
		
};
