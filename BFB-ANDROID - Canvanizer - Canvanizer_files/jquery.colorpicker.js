( function( $, undefined ) {
    //Attach this new method to jQuery
    $.fn.extend({ 
		
            //This is where you write your plugin's name
            colorpicker: function( options ) {

                    var defaults ={
                        target   : 'body',
                        display   :'list',
                        uidialog : false 
                    },
                    options =  $.extend(defaults, options);

                    var tpl = [
                        '<div class="color-picker-popup">',															
                                '<ul class="color-picker"></ul>',
                        '</div>'
                    ];

                    // init dialog box using color picker attr
                    var dialog_handler = { 

                        bind_close: function (){

                                if ( dialog_handler.clickoutside ) return ;


                                $('.ui-widget-overlay').live('click', function() {
                                        $(".ui-dialog").each( function() {

                                                var p = $( this ).find( '.color-picker-popup' ); 											
                                                if( p.length > 0  ){
                                                        $(  '#'+p.attr('id') ).dialog("close");
                                                }

                                        });
                                });	
                                
                                dialog_handler.clickoutside = true ;
                        },
                        clickoutside : false ,

                        dialog_init 	: function ( attr_id ){

                                $( attr_id ).dialog({
                                        title: 'Color picker' ,
                                        modal: true,
                                        autoOpen: false
                                });

                                dialog_handler.bind_close();
                        }
                    };

                    // Iterate over the current set of matched elements
                    return this.each(function() {

                        var obj = ( this );

                        if ( ! $(this).data("picker_id") ){

                                var html = $( tpl.join('') ),
                                        li = ['<li>',
                                                '<a class="color-label" ',
                                                'data-class="%%class%%" ',
                                                'data-hex="%%color%%" ',
                                                'style="background-color:%%color%%"></a>',
                                               '</li>'].join(''),
                                        colors = options.values;

                                for ( var i= 0 ; i <  colors.length  ; i++ ){											
                                        html.find('ul').append( li.replace( /%%color%%/g , colors[i] ) ) ;
                                } 
                                
                                for( var i in colors ){
                                    
                                        html.find('ul').append( 
                                            li.replace( /%%color%%/g , i )
                                              .replace( /%%class%%/g , colors[i] )
                                        );
                                }
                               

                                html.mouseleave( function(){
                                        // $(this).hide();
                                });

                                var picker_id  = 'picker_'+ Math.ceil( Math.random() * 1000000 ) ;
                                var attr_id    = '#'+ picker_id ;

                                html.attr('id' , picker_id );

                                if( options.display == 'grid' ){
                                        html.find('li').addClass('grid');	
                                }

                                $(this).data("picker_id", picker_id ); 

                                if( options.uidialog ) { 

                                        $('body').append( html );

                                        dialog_handler.dialog_init( attr_id );
                                }


                                if( $( options.target ).length > 0 ){

                                        html.css('display','block');

                                        $( options.target ).append( html );
                                }
                                
                                $( options.target ).find( 'a.color-label').live('click', function (){
                                        var $ul = $(this).closest('ul.color-picker');
                                        
                                        $ul.find('.color-label')
                                           .removeClass('selected');
                                   
                                        $(this).addClass('selected');


                                        options.click.apply( 
                                            obj , 
                                            [$(this).attr('data-hex'), $(this).attr('data-class') ]  
                                        );
                                });
                                
                                
                                

                        }


                        $(obj).click( function(){    

                                var dialog = '#'+$( this).data("picker_id"); 

                                if ( $(dialog).dialog( "isOpen" ) ) {		
                                    $(dialog).dialog('close');									
                                } else{		
                                    $(dialog).dialog('open');									
                                }

                                return false;
                        });            

                    });

            }
    });
})(jQuery);