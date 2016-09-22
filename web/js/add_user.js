/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        $(document).ready(function() {
            $("#signup").click(function() {

                resetFields();
                var emptyfields = $("input[value=]");
                if (emptyfields.size() > 0) {
                    emptyfields.each(function() {
                        $(this).stop()
                            .animate({ left: "-10px" }, 100).animate({ left: "10px" }, 100)
                            .animate({ left: "-10px" }, 100).animate({ left: "10px" }, 100)
                            .animate({ left: "0px" }, 100)
                            .addClass("required");
                    });
                }
            });
        });
        function resetFields() {
            $("input[type=text], input[type=password]").removeClass("required");
        }
    

