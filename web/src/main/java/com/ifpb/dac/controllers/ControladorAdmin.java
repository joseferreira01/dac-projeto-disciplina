package com.ifpb.dac.controllers;

import com.ifpb.dac.entidades.Pedido;
import com.ifpb.dac.entidades.Usuario;
import com.ifpb.dac.enums.Tipo;
import com.ifpb.dac.interfaces.PedidoDao;
import com.ifpb.dac.interfaces.UsuarioDao;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author rodrigobento
 */
@Named
@RequestScoped
public class ControladorAdmin implements Serializable {
    
    @Inject
    private UsuarioDao usuarioDao;
    @Inject
    private PedidoDao pedidoDao;
    private Usuario usuario = new Usuario();
    private List<Pedido> pedidos = new ArrayList<>();

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pedido> getPedidos() {
        return pedidoDao.listarTodos();
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    
    public String realizarLogin(){
        Usuario admin = usuarioDao.autentica(usuario.getEmail(), 
                usuario.getSenha(), Tipo.Administrador);
        if(admin != null){
            return "gerenciar.xhtml";
        } else {
            usuario = new Usuario();
            return null;
        }        
    }

    public String liberarAcesso(Pedido p){
        pedidoDao.remover(p);
        Usuario usuLiberado = usuarioDao.autentica(p.getEmail(), p.getSenha()
                , p.getTipo());
        if(usuLiberado != null){
            usuLiberado.setLogado(true);
            usuarioDao.atualizar(usuLiberado);
        }
        return null;
    }
    
    public void voltar(){
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        try {
            externalContext.redirect("../index.xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
