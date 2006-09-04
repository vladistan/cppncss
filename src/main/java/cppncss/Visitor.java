/**
 * Redistribution  and use  in source  and binary  forms, with  or without
 * modification, are permitted provided  that the following conditions are
 * met :
 *
 * . Redistributions  of  source  code  must  retain  the  above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * . Redistributions in  binary form  must reproduce  the above  copyright
 *   notice, this list of conditions  and the following disclaimer in  the
 *   documentation and/or other materials provided with the distribution.
 *
 * . The name of the author may not be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE  AUTHOR ``AS IS''  AND ANY EXPRESS  OR
 * IMPLIED  WARRANTIES,  INCLUDING,  BUT   NOT  LIMITED  TO,  THE   IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND  FITNESS FOR A PARTICULAR  PURPOSE ARE
 * DISCLAIMED.  IN NO  EVENT SHALL  THE AUTHOR  BE LIABLE  FOR ANY  DIRECT,
 * INDIRECT,  INCIDENTAL,  SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL  DAMAGES
 * (INCLUDING,  BUT  NOT LIMITED  TO,  PROCUREMENT OF  SUBSTITUTE  GOODS OR
 * SERVICES;  LOSS  OF USE,  DATA,  OR PROFITS;  OR  BUSINESS INTERRUPTION)
 * HOWEVER CAUSED  AND ON  ANY THEORY  OF LIABILITY,  WHETHER IN  CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY  WAY  OUT OF  THE  USE OF  THIS  SOFTWARE, EVEN  IF  ADVISED OF  THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cppncss;

import cppast.*;

/**
 * @author Mathieu Champlon
 * @version $Revision: $ $Date: $
 */
public class Visitor implements ParserVisitor
{
    /**
     * {@inheritDoc}
     */
    public Object visit( SimpleNode node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtranslation_unit node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexternal_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTclass_name node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTfunction_definition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTfunc_decl_def node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTlinkage_specification node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTusing_directive node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTusing_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnamespace_definition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdeclaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdeclaration_specifiers node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtype_modifiers node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTstorage_class_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtype_qualifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTbuiltin_type_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTscope_override_lookahead node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTscope_override node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTqualified_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTqualified_type node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTinit_declarator_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTinit_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTclass_head node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTclass_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTbase_clause node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTbase_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmember_specification node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTaccess_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmember_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmember_declarator_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTpure_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTconversion_function_decl_or_def node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTenum_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTenumerator_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTenumerator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTptr_operator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTcv_qualifier_seq node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdeclarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdirect_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdeclarator_suffixes node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTfunction_declarator_lookahead node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTfunction_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTfunction_direct_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdtor_ctor_decl_spec node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdtor_definition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTctor_definition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTctor_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTctor_initializer node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmem_initializer node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmem_initializer_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdtor_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTsimple_dtor_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTparameter_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTparameter_declaration_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTparameter_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTinitializer node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtype_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTabstract_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTabstract_declarator_suffix node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_head node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_parameter_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_parameter node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_argument_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_argument node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTstatement_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTstatement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdeclaration_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTblock_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnamespace_alias_definition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTsimple_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTlabeled_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTcompound_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTselection_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTiteration_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTcondition node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTjump_statement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtry_block node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASThandler node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexception_declaration node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTthrow_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexpression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTassignment_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTconstant_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTconditional_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTlogical_or_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTlogical_and_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTinclusive_or_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexclusive_or_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTand_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTequality_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTrelational_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTshift_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTadditive_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTmultiplicative_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTpm_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTcast_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTunary_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnew_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnew_placement node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnew_type_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnew_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdirect_new_declarator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTnew_initializer node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTdelete_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTunary_operator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTpostfix_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTsimple_type_specifier node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTid_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTunqualified_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASToperator_function_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTconversion_function_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtemplate_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTprimary_expression node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexpression_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTconstant node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASToperator node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTexception_spec node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTtype_id_list node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASTclass_key node, Object data )
    {
        return node.childrenAccept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( ASToperator_id node, Object data )
    {
        return node.childrenAccept( this, data );
    }
}
